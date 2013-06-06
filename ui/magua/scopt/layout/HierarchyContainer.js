/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


define([
    "dojo/_base/declare",
    "dojo/topic",
    "dojo/on",
    "dojo/dom-class",
    "dijit/layout/StackContainer",
    "dijit/layout/ContentPane"
], function(declare, topic, on, domClass, StackContainer, ContentPane) {
    return declare([StackContainer], {
        viewsByIds: {},
        selectedView: null,
        validateShowView: function(viewId) {
            return viewId;
        },
        publishParameters: function(viewId, parameters) {
        },
        startup: function() {
            this.inherited(arguments);

            this.loadingView = new ContentPane({
                content: "<span class='hierarchyContainerLoading dijitIconLoading'></span>"
            });
            this.addChild(this.loadingView);
            this.selectChild(this.loadingView);

            domClass.add(this.domNode, "hierarchyContainer");
            topic.publish(this.id + "/startup");

            if (this.views)
                this.setViews(this.views);
            if (this.connections)
                this.setConnections(this.connections);
        },
        setViews: function(views) {
            var viewsQueue = views.slice();
            while (viewsQueue.length > 0) {
                var s = viewsQueue.shift();
                if (s.view) {
                    this.viewsByIds[s.id] = {
                        class: s.view,
                        instance: null,
                        code: s.code
                    };
                } else if (s.views) {
                    viewsQueue = viewsQueue.concat(s.views);
                }
            }

            topic.publish(this.id + "/viewsChanged", views);
        },
        clearAllViews: function() {
            this.destroyDescendants(false);
            this.loadingView = new ContentPane({
                content: "<span class='hierarchyContainerLoading dijitIconLoading'></span>"
            });
            this.addChild(this.loadingView);
            this.selectChild(this.loadingView);

            for (var i in this.viewsByIds) {
                this.viewsByIds[i].instance = null;
            }
        },
        setConnections: function(connections) {
            this.connections = connections;
            for (var i in this.viewsByIds) {
                var view = this.viewsByIds[i];
                if (view.instance)
                    this._initConnections(view.instance);
            }
        },
        _initConnections: function(view) {
            var self = this;
            for (var i in this.connections) {
                var c = this.connections[i];
                if (c.sourceView === view.id) {
                    var targetViewId = c.targetView;
                    view[c.sourceEvent] = function() {
                        var targetView = self.selectView(targetViewId);
                        if (c.targetEvent)
                            targetView[c.targetEvent].apply(targetView, arguments);
                    };
                }
            }
        },
        _getView: function(id) {
            var view = this.viewsByIds[id];
            if (!view)
                return;
            var self = this;
            if (!view.instance) {
                require([view.class], function(View) {
                    view.instance = new View({
                        id: id,
                        code: view.code
                    });
                    view.instance.startup();
                    domClass.add(view.instance.domNode, "hierarchyContainer-child");
                    self.addChild(view.instance);
                    on(view.instance, "attrmodified-title", function(evt) {
                        if (self.selectedView === view.instance) {
                            topic.publish(self.id + "/viewTitleChanged", evt.newValue);
                        }
                    });
                    self._initConnections(view.instance);
                });
            }
            return view.instance;
        },
        selectView: function(id, parameters) {
            var currChild = this.selectChild;
            this.selectChild(this.loadingView);
            id = this.validateShowView(id);
            var view = this._getView(id);
            if (view) {
                topic.publish(this.id + "/selectView", view);
                topic.publish(this.id + "/viewTitleChanged", view.get("title"));
                this.selectedView = view;
                view.onSelect(parameters);
                this.selectChild(view);
                return view;
            } else {
                this.selectChild(currChild);
                return null;
            }
        }
    });
});
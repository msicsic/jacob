/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


define([
    "dojo/_base/declare",
    "dojo/i18n",
    "dijit/registry",
    "dojo/_base/array",
    "dijit/Menu",
    "dijit/MenuItem",
    "dijit/PopupMenuItem"
], function(declare, i18n, registry, array, Menu, MenuItem, PopupMenuItem) {
    return declare([Menu], {
        hierarchyContainer: null,
        postMixInProperties: function() {
            this.inherited(arguments);
            this.messages = i18n.getLocalization("scopt", "menu-items");
        },
        postCreate: function() {
            this.inherited(arguments);
            this.subscribe(this.hierarchyContainerId + "/startup", this._onStartup);
            this.subscribe(this.hierarchyContainerId + "/viewsChanged", this._onViewsChanged);
        },
        _onStartup: function() {
            this.hierarchyContainer = registry.byId(this.hierarchyContainerId);
        },
        _onViewsChanged: function(views) {
            for (var i in views) {
                this._buildMenu(this, views[i]);
            }
        },
        _buildMenu: function(parMenu, node) {
            if (node.hidden)
                return;
            var self = this;

            //druha urvoven je vertikalna
            if (node.view && node.id) {
                parMenu.addChild(new MenuItem({
                    label: self.messages[node.id],
                    onClick: function() {
                        self.hierarchyContainer.selectView(node.id);
                    }
                }));
            } else {
                var subMenu = new Menu();
                for (var i in node.views) {
                    this._buildMenu(subMenu, node.views[i]);
                }

                parMenu.addChild(new PopupMenuItem({
                    label: self.messages[node.id],
                    popup: subMenu
                }));
            }

        },
        _setDisabledAttr: function(value) {
            this._set("disabled", value);
            this.getChildren();
            array.forEach(this.getChildren(), function(child) {
                child.set("disabled", value);
            });
        }
    });
});
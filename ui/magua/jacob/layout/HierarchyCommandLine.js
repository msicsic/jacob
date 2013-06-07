/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


define([
    "dojo/_base/declare",
    "dojo/i18n",
    "dijit/registry",
    "dijit/form/ValidationTextBox",
    "dojo/on",
    "dojo/keys"
], function(declare, i18n, registry, ValidationTextBox, on, keys) {
    return declare([ValidationTextBox], {
        hierarchyContainer: null,
        viewIdByCodes: {},
        postCreate: function() {
            this.inherited(arguments);
            this.subscribe(this.hierarchyContainerId + "/startup", "onStartup");
            this.subscribe(this.hierarchyContainerId + "/viewsChanged", "onViewsChanged");
        },
        startup: function() {
            this.inherited(arguments);

            var messages = i18n.getLocalization("jacob", "HierarchyCommandLine");
            this.promptMessage = messages.promptMessage;
            this.invalidMessage = messages.invalidMessage;

            var self = this;
            on(this.domNode, "keypress", function(e) {
                switch (e.keyCode) {
                    case keys.ENTER:
                        self._submit();
                        break;
                    case keys.TAB:
                        self._autocomplete();
                        break;
                }
            });
        },
        validator: function(value) {
            if (value === "" || value === null) {
                return true;
            } else {
                var args = value.match(/([^\s]+)/g);
                if (args.length > 0) {
                    return args[0] in this.viewIdByCodes;
                } else {
                    return false;
                }
            }
        },
        onStartup: function() {
            this.hierarchyContainer = registry.byId(this.hierarchyContainerId);
        },
        onViewsChanged: function(views) {
            this.viewIdByCodes = {};
            for (var i in views) {
                this._buildCodes(views[i]);
            }
        },
        _buildCodes: function(node) {
            if (node.view && node.id && node.code) {
                this.viewIdByCodes[node.code] = node.id;
            } else {
                for (var i in node.views) {
                    this._buildCodes(node.views[i]);
                }
            }
        },
        _submit: function() {
            if (this.validate()) {
                var args = this.get("value").match(/([^\s]+)/g);
                hierarchyContainer.selectView(this.viewIdByCodes[args[0]], args.slice(1));
                this.set("value", "");
                this.focusNode.blur();
            }
        },
        _autocomplete: function() {
            //TODO
        }
    });
});
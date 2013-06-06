define([
    "dojo/_base/declare",
    "dijit/_WidgetBase",
    "dijit/_Container",
    "dijit/layout/_ContentPaneResizeMixin"
], function(declare, _WidgetBase, _Container, _ContentPaneResizeMixin) {
    return declare([_WidgetBase, _Container, _ContentPaneResizeMixin], {
        /**
         * Call on showed by HierarchyContainer
         * @param {Array} parameters
         * @returns {undefined}
         */
        onSelect: function(parameters) {
        },
        /**
         * Send hash parameters to HierarchyContainer
         * @param {Array} parameters
         * @returns {undefined}
         */
        publishHashParameters: function(parameters) {
            this.getParent().publishParameters(this.id, parameters);
        }
    });
});
/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

define([
    "dojo/_base/declare",
    "scopt/view/View",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "dojo/text!./templates/UpdateItemView.html"
], function(declare, View, _TemplatedMixin, _WidgetsInTemplateMixin, template) {
    return declare([View, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        title: "Update item",
        onSelect: function(parameters) {
            if (parameters) {
                this.onShowItem(parameters[0]);
            }
        },
        /*
         * events:
         */
        onShowItemDetail: function(itemId) {
            this.publishHashParameters([itemId]);
            var item = {
                id: itemId,
                name: "Mock name",
                description: "Mock description"
            };
            this.form.setValues(item);
        },
        /*
         * actions:
         */
        updateItem: function() {
            if (this.form.validate()) {
                alert("Updated " + this.nameInput.get("value"));
            }
        }
    });
});
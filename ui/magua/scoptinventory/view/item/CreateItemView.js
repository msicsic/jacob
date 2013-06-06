/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

define([
    "dojo/_base/declare",
    "scopt/view/View",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "dojo/text!./templates/CreateItemView.html"
], function(declare, View, _TemplatedMixin, _WidgetsInTemplateMixin, template) {
    return declare([View, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        title: "Create item",
        /*
         * events:
         */
        onShowItemDetail: function() {
        },
        /*
         * actions:
         */
        createItem: function() {
            
        }
    });
});
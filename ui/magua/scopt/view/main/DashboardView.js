define([
    "dojo/_base/declare",
    "scopt/view/View",
    "dijit/_TemplatedMixin",
    "dojo/text!./templates/DashboardView.html"
], function(declare, View, _TemplatedMixin, template) {
    return declare([View, _TemplatedMixin], {
        templateString: template
    });
});
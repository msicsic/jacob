define([
    "dojo/_base/declare",
    "dojo/i18n",
    "scopt/view/View",
    "dijit/_TemplatedMixin",
    "dojo/text!./templates/GreetingView.html"
], function(declare, i18n, View, _TemplatedMixin, template) {
    var messages = i18n.getLocalization("scopt", "GreetingView");
    
    return declare([View, _TemplatedMixin], {
        templateString: template,
        messages: messages
    });
});
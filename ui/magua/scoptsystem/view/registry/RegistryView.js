/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


define([
    "dojo/_base/declare",
    "dojo/i18n",
    "dojo/dom-class",
    "scopt/view/View",
    "scopt/connector",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "dojo/text!./templates/RegistryView.html"
], function(declare, i18n, domClass, View, connector, _TemplatedMixin, _WidgetsInTemplateMixin, template) {
    var messages = i18n.getLocalization("scoptsystem", "RegistryView");

    return declare([View, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        messages: messages,
        title: messages.registryTitle,
        onSelect: function(parameters) {
            if (parameters) {
                var itemId = parameters[0];
                if (itemId) {
                    var item = this.messagesStore.query({id: itemId})[0];
                    if (item) {
                        this.messagesTree.set("selectedItem", item);
                        this._showItem(itemId);
                    }
                }
            }
        },
        /*
         * actions:
         */
        _selectItem: function(item) {
            if (item.type === "message") {
                this._showItem(item.id);
            }
        },
        _showItem: function(itemId) {
            var self = this;
            connector.call("devel.mpu_get", {
                data: {
                    message_type: itemId
                }
            }).then(function(data) {
                self.publishHashParameters([itemId]);
                self.messageTitle.innerHTML = data.message_type;
                self.messageVersion.innerHTML = data.version;
                self.messageDoc.innerHTML = data.doc;
                self.messageRequest.set("value", data.request);
                self.messageResponse.set("value", data.response);
                domClass.remove(self.messageNode, "noneDisplay");
            });
        }
    });
});
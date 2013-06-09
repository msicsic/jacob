define([
    "dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/i18n",
    "dojo/dom-class",
    "jacob/dialog/Dialog",
    "dijit/_WidgetBase",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "jacob/store/DataStore",
    "jacob/connector",
    "dojo/text!./templates/PartnerContactDialog.html"
], function(declare, lang, i18n, domClass, Dialog, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, DataStore, connector, template) {
    var messages = i18n.getLocalization("jacobsystem", "PartnerContactDialog");
    lang.mixin(messages, i18n.getLocalization("jacob", "common"));

    var ContentWidget = declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        messages: messages,
        values: null,
        constructor: function() {
            this.inherited(arguments);

            this.mediaTypesStore = new DataStore({
                message: "settings.mediaTypeSelect"
            });
        },
        startup: function() {
            this.inherited(arguments);

            this.contactMediaTypeInput.setStore(this.mediaTypesStore);

            if (this.mode === "update") {
                domClass.add(this.createButton.domNode, "noneDisplay");
                this.form.setValues(this.values);
            } else {
                domClass.add(this.updateButton.domNode, "noneDisplay");
                domClass.add(this.deleteButton.domNode, "noneDisplay");
            }
        },
        _create: function() {
            var formValues = this.form.getValues();
            var self = this;
            connector.compoundCall([
                [
                    "settings.mediaContactCreate", {
                        messageId: "createContactMsg",
                        data: {
                            contact: formValues.mediaContact.contact
                        }
                    }
                ], [
                    "settings.businessPartnerAddMediaContact", {
                        data: {
                            businessPartnerId: this.values.businessPartnerId,
                            note: formValues.note,
                            mediaContactId: "${createContactMsg.resd.id}",
                            mediaTypeId: formValues.mediaType.id
                        }
                    }
                ]
            ]).then(function() {
                self.getParent().hide();
                self.getParent().onContactChanged();
            });
        },
        _update: function() {
            var formValues = this.form.getValues();
            var self = this;
            connector.compoundCall([
                [
                    "settings.mediaContactUpdate", {
                        data: {
                            id: this.values.mediaContact.id,
                            contact: formValues.mediaContact.contact
                        }
                    }
                ], [
                    "settings.businessPartnerUpdateMediaContact", {
                        data: {
                            businessPartnerId: this.values.businessPartnerId,
                            note: formValues.note,
                            mediaContactId: this.values.mediaContact.id,
                            mediaTypeId: formValues.mediaType.id
                        }
                    }
                ]
            ]).then(function() {
                self.getParent().hide();
                self.getParent().onContactChanged();
            });
        },
        _delete: function() {
            var self = this;
            connector.call("settings.businessPartnerRemoveMediaContact", {
                data: {
                    businessPartnerId: this.values.businessPartnerId,
                    mediaContactId: this.values.mediaContact.id
                }
            }).then(function() {
                self.getParent().hide();
                self.getParent().onContactChanged();
            });
        },
        _hide: function() {
            this.getParent().hide();
        }
    });

    return declare(Dialog, {
        values: null,
        mode: null,
        onContactChanged: function() {
        },
        startup: function() {
            this.inherited(arguments);

            if (this.mode === "update") {
                this.set("title", messages.partnerContact);
            } else {
                this.set("title", messages.createPartnerContact);
            }

            this.contentWidget = new ContentWidget({
                values: this.values,
                mode: this.mode
            });
            this.set("content", this.contentWidget);
        }
    });
});
define([
    "dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/i18n",
    "dojo/dom-class",
    "scopt/dialog/Dialog",
    "dijit/_WidgetBase",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "scopt/store/DataStore",
    "scopt/connector",
    "dojo/text!./templates/PartnerContactDialog.html"
], function(declare, lang, i18n, domClass, Dialog, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, DataStore, connector, template) {
    var messages = i18n.getLocalization("scoptsystem", "PartnerContactDialog");
    lang.mixin(messages, i18n.getLocalization("scopt", "common"));

    var ContentWidget = declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        messages: messages,
        values: null,
        constructor: function() {
            this.inherited(arguments);

            this.mediaTypesStore = new DataStore({
                message: "settings.media_type_select"
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
                    "settings.media_contact_create", {
                        messageId: "createContactMsg",
                        data: {
                            contact: formValues.media_contact.contact
                        }
                    }
                ], [
                    "settings.business_partner_add_media_contact", {
                        data: {
                            business_partner_id: this.values.business_partner_id,
                            note: formValues.note,
                            media_contact_id: "${createContactMsg.resd.id}",
                            media_type_id: formValues.media_type.id
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
                    "settings.media_contact_update", {
                        data: {
                            id: this.values.media_contact.id,
                            contact: formValues.media_contact.contact
                        }
                    }
                ], [
                    "settings.business_partner_update_media_contact", {
                        data: {
                            business_partner_id: this.values.business_partner_id,
                            note: formValues.note,
                            media_contact_id: this.values.media_contact.id,
                            media_type_id: formValues.media_type.id
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
            connector.call("settings.business_partner_remove_media_contact", {
                data: {
                    business_partner_id: this.values.business_partner_id,
                    media_contact_id: this.values.media_contact.id
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
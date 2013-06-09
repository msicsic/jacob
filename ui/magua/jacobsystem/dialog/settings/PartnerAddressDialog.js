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
    "dojo/text!./templates/PartnerAddressDialog.html"
], function(declare, lang, i18n, domClass, Dialog, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, DataStore, connector, template) {
    var messages = i18n.getLocalization("jacobsystem", "PartnerAddressDialog");
    lang.mixin(messages, i18n.getLocalization("jacobsystem", "address"));
    lang.mixin(messages, i18n.getLocalization("jacob", "common"));

    var ContentWidget = declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        messages: messages,
        values: null,
        constructor: function() {
            this.inherited(arguments);

            this.addressTypesStore = new DataStore({
                message: "settings.addressTypeSelect"
            });
        },
        startup: function() {
            this.inherited(arguments);

            this.addressTypeInput.setStore(this.addressTypesStore);

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
                    "settings.address_create", {
                        messageId: "createAddressMsg",
                        data: {
                            addressLine1: formValues.address.addressLine1,
                            addressLine2: formValues.address.addressLine2,
                            addressLine3: formValues.address.addressLine3,
                            city: formValues.address.city,
                            country: formValues.address.country,
                            state: formValues.address.state,
                            zip: formValues.address.zip
                        }
                    }
                ], [
                    "settings.business_partner_add_address", {
                        data: {
                            businessPartnerId: this.values.businessPartnerId,
                            note: formValues.note,
                            addressId: "${createAddressMsg.resd.id}",
                            addressTypeId: formValues.addressType.id
                        }
                    }
                ]
            ]).then(function() {
                self.getParent().hide();
                self.getParent().onAddressChanged();
            });
        },
        _update: function() {
            var formValues = this.form.getValues();
            var self = this;
            connector.compoundCall([
                [
                    "settings.address_update", {
                        data: {
                            id: this.values.address.id,
                            addressLine1: formValues.address.addressLine1,
                            addressLine2: formValues.address.addressLine2,
                            addressLine3: formValues.address.addressLine3,
                            city: formValues.address.city,
                            country: formValues.address.country,
                            state: formValues.address.state,
                            zip: formValues.address.zip
                        }
                    }
                ], [
                    "settings.businessPartnerUpdate_address", {
                        data: {
                            businessPartnerId: this.values.businessPartnerId,
                            note: formValues.note,
                            addressId: this.values.address.id,
                            addressTypeId: formValues.addressType.id
                        }
                    }
                ]
            ]).then(function() {
                self.getParent().hide();
                self.getParent().onAddressChanged();
            });
        },
        _delete: function() {
            var self = this;
            connector.call("settings.business_partner_remove_address", {
                data: {
                    businessPartnerId: this.values.businessPartnerId,
                    addressId: this.values.address.id
                }
            }).then(function() {
                self.getParent().hide();
                self.getParent().onAddressChanged();
            });
        },
        _hide: function() {
            this.getParent().hide();
        }
    });

    return declare(Dialog, {
        values: null,
        mode: null,
        onAddressChanged: function() {
        },
        startup: function() {
            this.inherited(arguments);

            if (this.mode === "update") {
                this.set("title", messages.partnerAddress);
            } else {
                this.set("title", messages.createPartnerAddress);
            }

            this.contentWidget = new ContentWidget({
                values: this.values,
                mode: this.mode
            });
            this.set("content", this.contentWidget);
        }
    });
});
define([
    "dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/i18n",
    "dojo/on",
    "dojo/dom-style",
    "jacob/connector",
    "jacob/view/View",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "dojo/store/Memory",
    "jacob/store/DataStore",
    "dojo/data/ObjectStore",
    "jacobsystem/dialog/settings/PartnerContactDialog",
    "jacobsystem/dialog/settings/PartnerAddressDialog",
    "dojo/text!./templates/PartnersView.html"
], function(declare, lang, i18n, on, domStyle, connector, View, _TemplatedMixin, _WidgetsInTemplateMixin, Memory, DataStore, ObjectStore, PartnerContactDialog, PartnerAddressDialog, template) {
    var messages = i18n.getLocalization("jacobsystem", "PartnersView");
    lang.mixin(messages, i18n.getLocalization("jacobsystem", "address"));
    lang.mixin(messages, i18n.getLocalization("jacob", "common"));

    return declare([View, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        messages: messages,
        title: messages.partnersTitle,
        constructor: function() {
            this.inherited(arguments);

            this.firstSelect = true;

            this.partnersStore = new DataStore({
                message: "settings.businessPartnerSelect"
            });
            this.mediaContactsStore = new Memory({});
            this.addressesStore = new Memory({});
        },
        startup: function() {
            this.inherited(arguments);
            var self = this;

            this.partnersGrid.set("structure", [[
                    {
                        name: messages.name,
                        field: "name",
                        width: "100%"
                    }
                ]]);
            this.partnersGrid.setStore(new ObjectStore({objectStore: this.partnersStore}));
            on(this.partnersGrid, "rowClick", function(event) {
                var parnerId = this.getItem(event.rowIndex).id;
                self._showPartner(parnerId);
            });

            this.mediaContactsGrid.set("structure", [[
                    {
                        name: messages.type,
                        field: "mediaType",
                        formatter: function(mediaType) {
                            return mediaType.name;
                        },
                        width: "20%"
                    }, {
                        name: messages.contact,
                        field: "mediaContact",
                        formatter: function(mediaContact) {
                            return mediaContact.contact;
                        },
                        width: "30%"
                    }, {
                        name: messages.note,
                        field: "note",
                        width: "50%"
                    }
                ]]);
            this.mediaContactsGrid.setStore(new ObjectStore({objectStore: this.mediaContactsStore}));
            on(this.mediaContactsGrid, "rowClick", function(event) {
                var partnerContact = this.getItem(event.rowIndex);
                self._showContact(partnerContact);
            });

            this.addressesGrid.set("structure", [[
                    {
                        name: messages.type,
                        field: "addressType",
                        formatter: function(addressType) {
                            return addressType.name;
                        },
                        width: "30%"
                    }, {
                        name: messages.note,
                        field: "note",
                        width: "70%"
                    }
                ]]);
            this.addressesGrid.setStore(new ObjectStore({objectStore: this.addressesStore}));
            on(this.addressesGrid, "rowClick", function(event) {
                var partnerAddress = this.getItem(event.rowIndex);
                self._showAddress(partnerAddress);
            });
        },
        onSelect: function(parameters) {
            this._showDetailInMode("none");

            if (this.firstSelect) {
                delete(this.firstSelect);
            } else {
                this.partnersStore.reloadData();
                this.partnersGrid.setQuery({});
                this.partnersGrid.selection.deselectAll();
                this.searchPartnerForm.reset();

                this.updatePartnerForm.reset();
                this.mediaContactsStore.setData([]);
                this.mediaContactsGrid.setQuery({});
                this.mediaContactsGrid.selection.deselectAll();
                this.addressesStore.setData([]);
                this.addressesGrid.setQuery({});
                this.addressesGrid.selection.deselectAll();
            }

            if (parameters) {
                var partnerId = parameters[0];
                if (partnerId) {
                    var partner = this.partnersStore.query({id: partnerId})[0];
                    if (partner) {
                        var rowIndex = this.partnersGrid.getItemIndex(partner);
                        this.partnersGrid.selection.addToSelection(rowIndex);
                        this._showPartner(partnerId);
                    }
                }
            }
        },
        /*
         * actions:
         */
        _searchPartner: function() {
            this.partnersStore.reloadData();

            var criteria = {
                name: this.searchPartnerNameInput.get("value")
            };
            if (this.searchPartnerIsCustomerInput.get("checked")) {
                criteria.isCustomer = true;
            }
            if (this.searchPartnerIsSupplierInput.get("checked")) {
                criteria.isSupplier = true;
            }
            this.partnersGrid.selection.deselectAll();
            this.partnersGrid.setQuery(criteria);

            this.filterOptionsPane.set("open", false, true);
            this.updatePartner = null;
            this._showDetailInMode("none");
        },
        _showPartner: function(partnerId) {
            this.partnerId = partnerId;
            this.publishHashParameters([this.partnerId]);

            var self = this;
            connector.compoundCall([
                [
                    "settings.businessPartnerSelect",
                    {
                        data: {id: this.partnerId}
                    }
                ], [
                    "settings.business_partner_mediaContact_list",
                    {
                        data: {businessPartnerId: this.partnerId}
                    }
                ], [
                    "settings.business_partner_address_list",
                    {
                        data: {businessPartnerId: this.partnerId}
                    }
                ]
            ], {
                sync: true
            }).then(function(datas) {
                var data = datas[0];
                self.updatePartner = data[0];
                self.updatePartnerNameInput.set("value", data[0].name);
                self.updatePartnerIsCustomerInput.set("checked", data[0].isCustomer);
                self.updatePartnerIsSupplierInput.set("checked", data[0].isSupplier);
                self._showDetailInMode("update");

                data = datas[1];
                self.mediaContactsStore.setData(data);
                self.mediaContactsGrid.setQuery({});

                data = datas[2];
                self.addressesStore.setData(data);
                self.addressesGrid.setQuery({});
            });
        },
        _showCreatePartner: function() {
            this.partnersGrid.selection.deselectAll();
            this._showDetailInMode("create");
            this.updatePartnerForm.reset();
        },
        _createPartner: function() {
            if (this.updatePartnerForm.validate()) {
                var self = this;
                connector.call("settings.business_partner_create", {
                    sync: true,
                    data: {
                        name: this.updatePartnerNameInput.get("value"),
                        isCustomer: this.updatePartnerIsCustomerInput.get("checked"),
                        isSupplier: this.updatePartnerIsSupplierInput.get("checked")
                    }
                }).then(function(data) {
                    self._searchPartner();
                    var partner = self.partnersStore.query({id: data.id})[0];
                    var rowIndex = self.partnersGrid.getItemIndex(partner);
                    self.partnersGrid.selection.addToSelection(rowIndex);
                    self._showPartner(data.id);
                });
            }
        },
        _showContact: function(partnerContact) {
            var self = this;
            var dialog = new PartnerContactDialog({
                mode: "update",
                values: partnerContact,
                onContactChanged: function() {
                    connector.call("settings.business_partner_mediaContact_list", {
                        data: {businessPartnerId: self.partnerId}
                    }).then(function(data) {
                        self.mediaContactsStore.setData(data);
                        self.mediaContactsGrid.setQuery({});
                    });
                }
            });

            dialog.show();
        },
        _createContact: function() {
            var self = this;
            var dialog = new PartnerContactDialog({
                mode: "create",
                values: {
                    businessPartnerId: this.partnerId
                },
                onContactChanged: function() {
                    connector.call("settings.business_partner_mediaContact_list", {
                        data: {businessPartnerId: self.partnerId}
                    }).then(function(data) {
                        self.mediaContactsStore.setData(data);
                        self.mediaContactsGrid.setQuery({});
                    });
                }
            });

            dialog.show();
        },
        _showAddress: function(partnerAddress) {
            var self = this;
            var dialog = new PartnerAddressDialog({
                mode: "update",
                values: partnerAddress,
                onAddressChanged: function() {
                    connector.call("settings.business_partner_address_list", {
                        data: {businessPartnerId: self.partnerId}
                    }).then(function(data) {
                        self.addressesStore.setData(data);
                        self.addressesGrid.setQuery({});
                    });
                }
            });

            dialog.show();
        },
        _createAddress: function() {
            var self = this;
            var dialog = new PartnerAddressDialog({
                mode: "create",
                values: {
                    businessPartnerId: this.partnerId
                },
                onAddressChanged: function() {
                    connector.call("settings.business_partner_address_list", {
                        data: {businessPartnerId: self.partnerId}
                    }).then(function(data) {
                        self.addressesStore.setData(data);
                        self.addressesGrid.setQuery({});
                    });
                }
            });

            dialog.show();
        },
        _updatePartner: function() {
            if (this.updatePartnerForm.validate()) {
                this.updatePartner.name = this.updatePartnerNameInput.get("value");
                this.updatePartner.isCustomer = this.updatePartnerIsCustomerInput.get("checked");
                this.updatePartner.isSupplier = this.updatePartnerIsSupplierInput.get("checked");

                var self = this;
                var parnerId = this.updatePartner.id;
                connector.call("settings.businessPartnerUpdate", {
                    sync: true,
                    data: this.updatePartner
                }).then(function() {
                    self._searchPartner();
                    self._showPartner(parnerId);
                });
            }
        },
        _cancelPartner: function() {
            this.updatePartner = null;
            this._showDetailInMode("none");
        },
        _showDetailInMode: function(mode) {
            switch (mode) {
                case "update":
                    domStyle.set(this.createPartnerButton.domNode, "display", "none");
                    domStyle.set(this.updatePartnerButton.domNode, "display", "");
                    domStyle.set(this.detailsTabContainer.domNode, "visibility", "");
                    domStyle.set(this.partnerDetailContainer.domNode, "visibility", "");
                    this.detailsTabContainer.selectChild(this.detailsTabContainer.getChildren()[0]);
                    break;
                case "create":
                    domStyle.set(this.createPartnerButton.domNode, "display", "");
                    domStyle.set(this.updatePartnerButton.domNode, "display", "none");
                    domStyle.set(this.detailsTabContainer.domNode, "visibility", "hidden");
                    domStyle.set(this.partnerDetailContainer.domNode, "visibility", "");
                    break;
                default:
                    domStyle.set(this.partnerDetailContainer.domNode, "visibility", "hidden");
            }
        }
    });
});
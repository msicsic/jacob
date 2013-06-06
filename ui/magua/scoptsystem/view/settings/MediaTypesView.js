define([
    "dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/i18n",
    "dojo/on",
    "scopt/connector",
    "scopt/view/View",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "dojo/data/ObjectStore",
    "scopt/store/DataStore",
    "dijit/form/ValidationTextBox",
    "scopt/dialog/MessageBoxDialog",
    "dojo/text!./templates/MediaTypesView.html"
], function(declare, lang, i18n, on, connector, View, _TemplatedMixin, _WidgetsInTemplateMixin, ObjectStore, DataStore, ValidationTextBox, MessageBoxDialog, template) {
    var messages = i18n.getLocalization("scoptsystem", "MediaTypesView");
    lang.mixin(messages, i18n.getLocalization("scopt", "common"));

    return declare([View, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        messages: messages,
        title: messages.mediaTypesTitle,
        constructor: function() {
            this.inherited(arguments);
            
            this.firstSelect = true;

            this.mediaTypesStore = new DataStore({
                message: "settings.media_type_select"
            });
        },
        startup: function() {
            this.inherited(arguments);

            dojo.require("dojox.grid.cells.dijit"); //FIXME: AMD include!
            this.mediaTypesGrid.set("structure", [[
                    {
                        name: messages.name,
                        field: "name",
                        editable: true,
                        type: dojox.grid.cells._Widget,
                        widgetClass: ValidationTextBox,
                        constraint: {required: true},
                        width: "100%"
                    }
                ]]);

            var oldName = null;
            on(this.mediaTypesGrid, "startEdit", function(inCell, inRowIndex) {
                var item = this.getItem(inRowIndex);
                oldName = item.name;
            });
            var self = this;
            on(this.mediaTypesGrid, "applyEdit", function(inRowIndex) {
                var item = this.getItem(inRowIndex);
                if (oldName !== item.name) {
                    MessageBoxDialog.question({
                        message: messages.confirmUpdate
                    }).then(function(btn) {
                        if (btn === "yes") {
                            connector.call("settings.media_type_update", {
                                sync: true,
                                data: {
                                    id: item.id,
                                    name: lang.trim(item.name)
                                }
                            });
                            self._refreshDataGrid();
                        } else {
                            item.name = oldName;
                            self.mediaTypesGrid.updateRow(inRowIndex);
                        }
                    });
                }
            });
            this.mediaTypesGrid.setStore(new ObjectStore({objectStore: this.mediaTypesStore}));
        },
        onSelect: function() {
            if (this.firstSelect) {
                delete(this.firstSelect);
            } else {
                this._refreshDataGrid();
            }
        },
        /*
         * actions:
         */
        _refreshDataGrid: function() {
            this.mediaTypesStore.reloadData();
            this.mediaTypesGrid.setQuery({});
        },
        _create: function() {
            if (this.createForm.validate()) {
                var self = this;
                connector.call("settings.media_type_create", {
                    sync: true,
                    data: {
                        name: this.mediaTypeNameInput.get("value")
                    }
                }).then(function() {
                    self._refreshDataGrid();
                    self.createForm.reset();
                });
            }
        }
    });
});
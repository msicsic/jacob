define([
    "dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/i18n",
    "dojo/on",
    "jacob/connector",
    "jacob/view/View",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "dojo/data/ObjectStore",
    "jacob/store/DataStore",
    "dijit/form/ValidationTextBox",
    "jacob/dialog/MessageBoxDialog",
    "dojo/text!./templates/UnitOfMeasuresView.html"
], function(declare, lang, i18n, on, connector, View, _TemplatedMixin, _WidgetsInTemplateMixin, ObjectStore, DataStore, ValidationTextBox, MessageBoxDialog, template) {
    var messages = i18n.getLocalization("jacobsystem", "UnitOfMeasuresView");
    lang.mixin(messages, i18n.getLocalization("jacob", "common"));

    return declare([View, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        messages: messages,
        title: messages.unitOfMeasuresTitle,
        constructor: function() {
            this.inherited(arguments);
            
            this.firstSelect = true;
            
            this.unitOfMeasuresStore = new DataStore({
                message: "settings.uomSelect"
            });
        },
        startup: function() {
            this.inherited(arguments);

            dojo.require("dojox.grid.cells.dijit"); //FIXME: AMD include!
            this.unitOfMeasuresGrid.set("structure", [[
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
            on(this.unitOfMeasuresGrid, "startEdit", function(inCell, inRowIndex) {
                var item = this.getItem(inRowIndex);
                oldName = item.name;
            });
            var self = this;
            on(this.unitOfMeasuresGrid, "applyEdit", function(inRowIndex) {
                var item = this.getItem(inRowIndex);
                if (oldName !== item.name) {
                    MessageBoxDialog.question({
                        message: messages.confirmUpdate
                    }).then(function(btn) {
                        if (btn === "yes") {
                            connector.call("settings.uomUpdate", {
                                sync: true,
                                data: {
                                    id: item.id,
                                    name: lang.trim(item.name)
                                }
                            });
                            self._refreshDataGrid();
                        } else {
                            item.name = oldName;
                            self.unitOfMeasuresGrid.updateRow(inRowIndex);
                        }
                    });
                }
            });
            this.unitOfMeasuresGrid.setStore(new ObjectStore({objectStore: this.unitOfMeasuresStore}));
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
            this.unitOfMeasuresStore.reloadData();
            this.unitOfMeasuresGrid.setQuery({});
        },
        _create: function() {
            if (this.createForm.validate()) {
                var self = this;
                connector.call("settings.uomCreate", {
                    sync: true,
                    data: {
                        name: this.unitOfMeasureNameInput.get("value")
                    }
                }).then(function() {
                    self._refreshDataGrid();
                    self.createForm.reset();
                });
            }
        }
    });
});
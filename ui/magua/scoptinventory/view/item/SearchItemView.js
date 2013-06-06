/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

define([
    "dojo/_base/declare",
    "dojo/i18n",
    "scopt/connector",
    "scopt/view/View",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "dojo/text!./templates/SearchItemView.html",
    "dojo/data/ObjectStore",
    "dojo/store/Memory",
    "scopt/store/DataStore",
    "scopt/store/QueryDataStore"
], function(declare, i18n, connector, View, _TemplatedMixin, _WidgetsInTemplateMixin, template, ObjectStore, Memory, DataStore, QueryDataStore) {
    var messages = i18n.getLocalization("scoptinventory", "SearchItemView");

    return declare([View, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        messages: messages,
        title: messages.searchItemTitle,
        constructor: function() {
            this.inherited(arguments);

//            this.itemSuppliersStore = new DataStore({
//                message: "inventory.supplier",
//                idProperty: "id",
//                requestData: {
//                    attributes: ["id", "name"],
//                    sort: ["+name"],
//                    condition: {}
//                }
//            });
//
//            this.itemsStore = new QueryDataStore({
//                message: "inventory.item_search",
//                idProperty: "id",
//                itemsProperty: "items",
//                totalProperty: "total"
//            });
//            this.inventoryStatusStore = new Memory();
//            this.suppliersStore = new Memory();
        },
        postCreate: function() {
            this.inherited(arguments);

//            this.itemSupplierInput.setStore(this.itemSuppliersStore);
//
//            this.itemsGrid.set("structure", [[
//                    {name: this.messages.internalCode, field: 'sku', 'width': '25%'},
//                    {name: this.messages.name, field: 'name', 'width': '40%'},
//                    {name: this.messages.count, field: 'qty', 'width': '20%'},
//                    {name: this.messages.unit, field: 'uom', 'width': '10%'},
//                    {name: this.messages.unitPrice, field: 'uom_price', 'width': '25%'}
//                ]]);
//            this.itemsGrid.set("store", new ObjectStore({objectStore: this.itemsStore}));
//
//            this.inventoryStatusGrid.set("structure", [[
//                    {name: this.messages.inventory, field: 'name', 'width': '65%'},
//                    {name: this.messages.count, field: 'count', 'width': '25%'},
//                    {name: this.messages.unit, field: 'unit', 'width': '10%'}
//                ]]);
//            this.inventoryStatusGrid.set("store", new ObjectStore({objectStore: this.inventoryStatusStore}));
//
//            this.suppliersGrid.set("structure", [[
//                    {name: this.messages.name, field: 'name', 'width': '50%'},
//                    {name: this.messages.deliveryTime, field: 'deliveryTime', 'width': '25%'},
//                    {name: this.messages.invertoryStatus, field: 'status', 'width': '25%'}
//                ]]);
//            this.suppliersGrid.set("store", new ObjectStore({objectStore: this.suppliersStore}));
        },
        onSelect: function(parameters) {
//            this.searchItemForm.reset();
//            if (parameters) {
//                var inputsArray = [this.itemInternalCodeInput, this.itemNameInput, this.itemSupplierCodeInput];
//                for (var i in parameters.slice(0, inputsArray.length)) {
//                    inputsArray[i].set("value", parameters[i]);
//                }
//            }
//
//            this._searchItems();
        },
        /* 
         * publish events:
         */
        onShowItemDetail: function() {
        },
        /* 
         * actions:
         */
        _showItemOverview: function(event) {
            var code = this.itemsGrid.getItem(event.rowIndex).code;

            this.inventoryStatusStore.setData([{
                    id: code,
                    name: "Name " + code,
                    count: Math.round(Math.random() * 1000),
                    unit: "kg"
                }]);
            this.inventoryStatusGrid.setQuery({});

            this.suppliersStore.setData([{
                    id: code,
                    name: "Name " + code,
                    deliveryTime: Math.round(Math.random() * 100),
                    status: Math.round(Math.random() * 2000)
                }]);
            this.suppliersGrid.setQuery({});
        },
        _showItemDetail: function() {

        },
        _searchItems: function() {
            var criteria = {
                sku: this.itemInternalCodeInput.get("value"),
                name: this.itemNameInput.get("value"),
                ssku: this.itemSupplierCodeInput.get("value")
            };
            //zatial nema realne IDcka
            if (this.itemSupplierInput.get("value") !== "") {
                criteria.supplier_id = this.itemSupplierInput.get("value");
            }

            this.itemsGrid.setQuery(criteria);
            this.inventoryStatusStore.setData([]);
            this.inventoryStatusGrid.setQuery({});
            this.suppliersStore.setData([]);
            this.suppliersGrid.setQuery({});

            this.filterOptionsPane.set("open", false, true);
        }
    });
});
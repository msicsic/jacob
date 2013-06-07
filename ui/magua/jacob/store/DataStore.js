define([
    "dojo/_base/declare",
    "dojo/store/api/Store",
    "dojo/_base/lang",
    "dojo/store/util/QueryResults",
    "jacob/connector",
    "dojo/store/util/SimpleQueryEngine"
], function(declare, Store, lang, QueryResults, connector, SimpleQueryEngine) {
    return declare([Store], {
        data: null,
        index: null,
        idProperty: "id",
        itemsProperty: null,
        queryEngine: SimpleQueryEngine,
        constructor: function(options) {
            lang.mixin(this, options);

            this.reloadData();
        },
        query: function(query, options) {
            return QueryResults(this.queryEngine(query, options)(this.data));
        },
        get: function(id) {
            return this.index[id];
        },
        put: function(object, options) {
            //takato funkcnost je asi na 2 veci ??
            var data = this.data;
            var index = this.index;
            var idProperty = this.idProperty;
            var id = object[idProperty] = (options && "id" in options) ? options.id : idProperty in object ? object[idProperty] : Math.random();
            if (id in index) {
                // object exists
                if (options && options.overwrite === false) {
                    throw new Error("Object already exists");
                }
                // replace the entry in data
                data[index[id]] = object;
            } else {
                // add the new object
                index[id] = data.push(object) - 1;
            }
            return id;
        },
        add: function(object, options) {
            //takato funckst je asi na 2 veci ??
            (options = options || {}).overwrite = false;
            return this.put(object, options);
        },
        remove: function(id) {
            var index = this.index;
            var data = this.data;
            if (id in index) {
                data.splice(index[id], 1);
                // now we have to reindex
                this.setData(data);
                return true;
            }
        },
        getIdentity: function(object) {
            //HACK dijit Select-om vadi ked je numericka hodnota
            return String(object[this.idProperty]);
        },
        setData: function(data) {
            this.data = data;
            this.index = {};
            for (var i in data) {
                var object = data[i];
                this.index[object[this.idProperty]] = object;
            }
        },
        reloadData: function() {
            var self = this;
            connector.call(this.message, {
                sync: true,
                data: this.requestData
            }).then(function(data) {
                if (self.itemsProperty) {
                    self.setData(data[self.itemsProperty]);
                } else {
                    self.setData(data);
                }
            });
        }
    });
});
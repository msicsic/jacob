define([
    "dojo/_base/declare",
    "dojo/on",
    "dojo/dom-class",
    "dijit/TitlePane"
], function(declare, on, domClass, TitlePane) {
    return declare([TitlePane], {
        buildRendering: function() {
            this.inherited(arguments);

            domClass.add(this.domNode, "owerflowTitlePane");
            
            var self = this;
            on(this, "blur", function() {
                self.set("open", false, true);
            });
        }
    });
}
);
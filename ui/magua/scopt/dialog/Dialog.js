/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

define([
    "dojo/_base/declare",
    "dojo/_base/array",
    "dojo/on",
    "dijit/Dialog"
], function(declare, array, on, Dialog) {

    var reorderDialogQueue = [];

    var ScoptDialog = declare(Dialog, {
        reorderToFront: false,
        show: function() {
            if (this.reorderToFront) {
                reorderDialogQueue.push(this);
                var self = this;
                on(this, "hide", function() {
                    var idx = reorderDialogQueue.indexOf(self);
                    if (-1 !== idx) {
                        reorderDialogQueue.splice(idx, 1);
                    }
                });
            }

            this.inherited(arguments);

            if (!this.reorderToFront) {
                array.forEach(reorderDialogQueue, function(dialog) {
                    Dialog._DialogLevelManager.hide(dialog);
                    Dialog._DialogLevelManager.show(dialog);
                });
            }
        }
    });

    ScoptDialog.getOpenedCount = function() {
        return Dialog._dialogStack.length - 1;
    };

    return ScoptDialog;
});
/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

define([
    "dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/i18n",
    "dojo/dom-class",
    "dojo/on",
    "scopt/dialog/Dialog",
    "dijit/_WidgetBase",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "dijit/form/Button",
    "dojo/Deferred",
    "dojo/text!./templates/MessageBoxDialog.html"
], function(declare, lang, i18n, domClass, on, Dialog, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Button, Deferred, template) {
        
    var messages = i18n.getLocalization("scopt", "MessageBoxDialog");
        
    var ContentWidget = declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        startup: function() {
            this.inherited(arguments);
            
            var dialog = this.getParent();

            this.messageNode.innerHTML = dialog.message;
            if (this.iconClass)
                domClass.add(this.iconNode, dialog.iconClass);
            else
                domClass.add(this.iconNode, "noneDisplay");

            for (var i in dialog.buttons) {
                var buttonName = dialog.buttons[i];
                var buttonNls = "button" + buttonName.charAt(0).toUpperCase() + buttonName.slice(1);
                var button = new Button({
                    label: messages[buttonNls],
                    name: buttonName,
                    onClick: function() {
                        dialog.returnButton = this.name;
                        dialog.hide();
                    }
                });
                button.placeAt(this.actionBarNode);
            }
        }
    });

    var MessageBoxDialog = declare(Dialog, {
        message: "",
        buttons: [],
        returnButton: "cancel",
        style: "min-width: 240px; max-width: 620px;",
        reorderToFront: true,
        constructor: function(options) {
            this.inherited(arguments);
            
            lang.mixin(this, options);

            var self = this;
            on(this, "hide", function() {
                self.onHideMessageBox(self.returnButton);
            });
        },
        startup: function() {
            this.inherited(arguments);
    
            this.contentWidget = new ContentWidget();
            this.set("content", this.contentWidget);
        },
        onHideMessageBox: function(button) {
        }
    });

    MessageBoxDialog.show = function(options) {
        var deferred = new Deferred();
        options.onHideMessageBox = function(button) {
            deferred.resolve(button);
        };
        var dialog = new MessageBoxDialog(options);
        dialog.show();
        return deferred.promise;
    };
    MessageBoxDialog.critical = function(options) {
        options = lang.mixin({
            iconClass: "messageBoxDialogIconCritical",
            title: messages.critical,
            buttons: ["ok"]
        }, options);
        return MessageBoxDialog.show(options);
    };
    MessageBoxDialog.warning = function(options) {
        options = lang.mixin({
            iconClass: "messageBoxDialogIconWarning",
            title: messages.warning,
            buttons: ["ok"]
        }, options);
        return MessageBoxDialog.show(options);
    };
    MessageBoxDialog.question = function(options) {
        options = lang.mixin({
            iconClass: "messageBoxDialogIconQuestion",
            title: messages.question,
            buttons: ["yes", "no"]
        }, options);
        return MessageBoxDialog.show(options);
    };
    MessageBoxDialog.information = function(options) {
        options = lang.mixin({
            iconClass: "messageBoxDialogIconInformation",
            title: messages.information,
            buttons: ["ok"]
        }, options);
        return MessageBoxDialog.show(options);
    };

    return MessageBoxDialog;
});
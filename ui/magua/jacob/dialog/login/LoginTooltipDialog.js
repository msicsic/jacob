/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

define([
    "dojo/_base/declare",
    "dojo/_base/kernel",
    "dojo/i18n",
    "dijit/popup",
    "dijit/TooltipDialog",
    "dijit/_WidgetBase",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "jacob/app",
    "jacob/storage",
    "dojo/text!./templates/LoginTooltipDialog.html"
], function(declare, kernel, i18n, popup, TooltipDialog, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, app, storage, template) {

    var messages = i18n.getLocalization("jacob", "LoginTooltipDialog");

    var ContentWidget = declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        messages: messages,
        constructor: function() {
            this.inherited(arguments);

            this.userName = storage.localGet("loginTooltipDialog.userName");
            this.currentLanguage = kernel.locale;
        },
        clear: function() {
            this.form.reset();
            this.userNameInput.set("value", storage.localGet("loginTooltipDialog.userName"));
            this.errorNode.innerHTML = "";
        },
        /*
         * actions:
         */
        _login: function() {
            if (this.form.validate()) {
                var userName = this.userNameInput.get("value");
                var userPassword = this.userPasswordInput.get("value");
                var dialog = this.getParent();
                var self = this;
                app.login(userName, userPassword).then(function() {
                    storage.localPut("loginTooltipDialog.userName", userName);
                    popup.close(dialog);
                }, function(message) {
                    self.errorNode.innerHTML = message;
                });
            }
            return false; //disable send form
        },
        _changeLanguage: function(value) {
            app.changeLocale(value);
        }
    });

    return declare(TooltipDialog, {
        style: "width: 340px;",
        startup: function() {
            this.inherited(arguments);

            this.contentWidget = new ContentWidget();
            this.set("content", this.contentWidget);
        },
        onShow: function() {
            this.contentWidget.clear();
        }
    });
});
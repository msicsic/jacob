define([
    "dojo/_base/declare",
    "dojo/i18n",
    "dojo/dom-class",
    "dojo/dom-style",
    "dojo/on",
    "dojo/fx",
    "dojo/keys",
    "dijit/focus",
    "jacob/app",
    "jacob/dialog/Dialog",
    "dijit/_WidgetBase",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "dojo/text!./templates/FooterWidget.html"
], function(declare, i18n, domClass, domStyle, on, fx, keys, focus, app, Dialog, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, template) {
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        postMixInProperties: function() {
            this.inherited(arguments);
            this.messages = i18n.getLocalization("jacob", "FooterWidget");
        },
        postCreate: function() {
            this.inherited(arguments);

            this.subscribe("/app/logout", this._onLogout);
            this.subscribe("/app/tenantChanged", this._onChangeTenant);
            var self = this;
            this.subscribe("hierarchyContainer/selectView", function(view) {
                self.viewCode.innerHTML = view.code || "";
            });
        },
        startup: function() {
            this.inherited(arguments);

            this.comandLine.set("disabled", true);
            var self = this;
            on(this.comandLine, "blur", function() {
                self._hideCommandLine();
            });
            on(this.comandLine, "keydown", function(e) {
                if (e.keyCode === keys.ESCAPE) {
                    self._hideCommandLine();
                }
            });

            if (!app.getTenant()) {
                domClass.add(this.commandLineButton, "commandLineButtonDisabled");
            } else {
                this._onChangeTenant();
            }
        },
        _onLogout: function() {
            domClass.add(this.commandLineButton, "commandLineButtonDisabled");
            if (this.showHandle1) {
                this.showHandle1.remove();
                this.showHandle2.remove();
                this.showHandle1 = null;
                this.showHandle2 = null;
            }
            domStyle.set(this.domNode, "borderTopColor", "transparent");
        },
        _onChangeTenant: function( ) {
            domClass.remove(this.commandLineButton, "commandLineButtonDisabled");
            if (!this.showHandle1) {
                var self = this;
                this.showHandle1 = on(this.commandLineButton, "click", function() {
                    self._showCommandLine();
                });
                this.showHandle2 = on(window, "keydown", function(e) {
                    if (e.shiftKey && String.fromCharCode(e.keyCode) === "C") {
                        var notInputFucused = !focus.curNode || ["input", "textarea"].indexOf(focus.curNode.nodeName.toLowerCase()) === -1;
                        if (notInputFucused && !Dialog.getOpenedCount()) {
                            self._showCommandLine();
                            e.preventDefault();
                        }
                    }
                });
            }
            var tenant = app.getTenant();
            domStyle.set(this.domNode, "borderTopColor", tenant.color || "transparent");
        },
        _showCommandLine: function() {
            var anim = fx.slideTo({
                node: this.comandLine.domNode,
                duration: 250,
                left: 6,
                top: 7,
                unit: "px"
            });

            this.comandLine.set("value", "");
            this.comandLine.set("disabled", false);
            var self = this;
            on(anim, "End", function() {
                self.comandLine.focus();
            });
            anim.play();
        },
        _hideCommandLine: function() {
            this.comandLine.focusNode.blur();
            this.comandLine.set("disabled", false);
            fx.slideTo({
                node: this.comandLine.domNode,
                duration: 250,
                left: -310,
                top: 7,
                unit: "px"
            }).play();
        }
    });
});
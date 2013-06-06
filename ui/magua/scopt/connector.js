define([
    "dojo/_base/lang",
    "dojo/_base/kernel",
    "dojo/json",
    "dojo/i18n",
    "dojo/topic",
    "dojo/request",
    "scopt/storage",
    "dojo/Deferred",
    "scopt/dialog/MessageBoxDialog"
], function(lang, kernel, json, i18n, topic, request, storage, Deferred, MessageBoxDialog) {
    var errorMessages = i18n.getLocalization("scopt", "errors");
    var header = lang.mixin(storage.sessionGet("connector.header"), {
        format: "scopt",
        version: "0.1",
        language: kernel.locale
    });

    function destorySecurityToken() {
        header = {
            format: "scopt",
            version: "0.1",
            language: kernel.locale
        };
        storage.sessionRemove("connector.header");

        topic.publish("/connector/logout");
    }

    return {
        login: function(login, password) {
            var deferred = new Deferred();

            header.security = {
                type: "security.authenticate.login.password",
                login: login,
                password: password
            };

            this.call(undefined, {
                noErrorLog: true,
                noErrorDialog: true
            }).then(function(data) {
                header.security = {
                    type: "security.flyby.token",
                    value: data.token
                };
                storage.sessionPut("connector.header", header);
                deferred.resolve(data.principal);
            }, function(error) {
                deferred.reject(error.message);
            });

            return deferred;
        },
        logout: function() {
            header.security.type = "security.invalidate_token";

            this.call(undefined, {
                noErrorLog: true,
                noErrorDialog: true
            }).then(function() {
                destorySecurityToken();
            }).then(function() {
                destorySecurityToken();
            });
        },
        compoundCall: function(messages, options) {
            if (options === undefined || options === null) {
                options = {};
            }

            options.data = [];
            for (var i in messages) {
                var messageType = messages[i][0];
                var messageOptions = messages[i][1] || {};

                options.data.push({
                    reqh: lang.mixin(lang.clone(header), {
                        type: messageType,
                        message_id: messageOptions.messageId || "msg_" + Math.round(Math.random() * 100000)
                    }),
                    reqd: messageOptions.data || {}
                });
            }

            var deferred = new Deferred();
            this.call("business.compound_request", options).then(function(data) {
                var datas = [];
                for (var i in data) {
                    datas.push(data[i].resd);
                }
                deferred.resolve(datas);
            }, function(err) {
                deferred.reject(err);
            });
            return deferred;
        },
        call: function(type, options) {
            if (options === undefined || options === null) {
                options = {};
            }
            var deferred = new Deferred();

            request.post("/core", {
                data: {
                    m: json.stringify({
                        reqh: lang.mixin(lang.clone(header), {
                            type: type,
                            message_id: options.messageId || "msg_" + Math.round(Math.random() * 100000)
                        }),
                        reqd: options.data || {}
                    }, null, "  ")
                },
                sync: options.sync,
                handleAs: "json"
            }).then(function(data) {
                if (data.resh) {
                    if (data.resh.status === "OK") {
                        deferred.resolve(data.resd);
                    } else {
                        if (data.resd.code === "security.invalid.token") {
                            MessageBoxDialog.warning({
                                message: errorMessages.securityTokenInvalidated
                            }).then(function() {
                                destorySecurityToken();
                                deferred.reject({
                                    code: data.resd.code,
                                    status: data.resh.status,
                                    message: data.resd.text
                                });
                            });
                        } else {
                            if (options.noErrorLog !== true) {
                                console.error("Error call message '" + type + "' erro code '" + data.resd.code + "'");
                            }
                            if (options.noErrorDialog !== true) {
                                MessageBoxDialog.critical({
                                    message: data.resd.text
                                });
                            }
                            deferred.reject({
                                code: data.resd.code,
                                status: data.resh.status,
                                message: data.resd.text
                            });
                        }
                    }
                } else {
                    if (options.noErrorLog !== true) {
                        console.error("Invalid response format from message '" + type + "'");
                    }
                    if (options.noErrorDialog !== true) {
                        MessageBoxDialog.critical({
                            message: errorMessages.invalidResponseFormat
                        });
                    }
                    deferred.reject({
                        code: "invalid.response.json",
                        message: errorMessages.invalidResponseFormat
                    });
                }
            }, function(error) {
                MessageBoxDialog.warning({
                    message: errorMessages.networkError
                }).then(function() {
                    destorySecurityToken();
                });
                deferred.reject({
                    code: "invalid.response.json",
                    message: errorMessages.invalidResponseFormat
                });
            });
            return deferred.promise;
        },
        setHeaderParameter: function(key, value) {
            header[key] = value;
            storage.sessionPut("connector.header", header);
        }
    };
});

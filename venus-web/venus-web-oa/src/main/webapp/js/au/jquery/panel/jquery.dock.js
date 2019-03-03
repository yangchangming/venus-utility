(function($) {
    var _settings;
    var _elements = { "top": null, "right": null, "bottom": null, "left": null };
    var _container;
    var _height;
    var _width;
    var _images;
    var _timer;
    var _open = false;
    var _close = false;
    var _element;
    var _previewDiv;
    var _animate = true;
    var _visible;
    var lblMessage;
    var _inDrag = false;

    $.fn.dock = function(settings) {
        _settings = $.extend({
            easing: "linear",
            duration: 200,
            expansion: 100,
            delay: 100,
            element_visibleIndex: 0,
            scrollEasing: "easeOutExpo",
            cursorAt: { left: 25, top: 25 }
        }, settings || {});

        lblMessage = $("#lblMessage");
        _dockers = this;
        _elements["top"] = this.slice(0, 1);
        _elements["right"] = this.slice(1, 2);
        _elements["bottom"] = this.slice(2, 3);
        _elements["left"] = this.slice(3, 4);
        _height = getDocumentHeight();
        _width = getDocumentWidth();
        _container = $("body");
        _previewDiv = create_previewDiv();
        createImages();
        lblMessage.val("init");
        initialize();
        _visible = $(this.get(_settings.element_visibleIndex)).show();
        this.not(_visible).hide();

        $(window).bind("resize", function() {
            var newWidth = getDocumentWidth();
            var newHeight = getDocumentHeight();
            if (newHeight !== _height && newWidth !== _width) {
                lblMessage.val("resize");
                _height = newHeight;
                _width = newWidth;
                positionImages();
                initDockers();
            }
        });

        $(window).bind("scroll", function(e) {
            _animate = false;
            clearTimeout(_timer);
            _timer = setTimeout(function() {
                $().stop();
                _visible.animate({ top: $(window).scrollTop() + _visible.data("originalTop") }, 800, _settings.scrollEasing, function() {
                    _animate = true;
                    _height = getDocumentHeight();
                    _width = getDocumentWidth();
                    positionImages();
                });
            }, 250);
        });

        $.fn.val = function(value) {
            if (location.href.indexOf("debug") == -1) return;
            var val = $(this).get(0).innerHTML;
            $(this).get(0).innerHTML = val + value + "<br />";
        };

        _images = _container.find("span.dock_image");
        _images.droppable({ tolerance: "pointer", "deactivate": function() { restoreDefaults(_visible); }, "drop": function(event, ui) { _expanded = false; _visible.hide(); _visible = restoreDefaults(_elements[$(this).data("position")]); positionDockers(); } })
        .hide()
        .mouseover(function() {
            $(this).addClass("dock_image_hover");
            positionPreview(this);
        })
        .mouseout(function() {
            $(this).removeClass("dock_image_hover");
            _previewDiv.hide();
        });

        this.draggable({ cursor: "move", helper: "clone", "cursorAt": _settings.cursorAt, "containment": "documnet", opacity: 1, scroll: false, drag: function() { $(this).hide(); _visible.hide(); }, start: function() { _inDrag = true; $().stop(); $(this).hide(); _animate = false; _images.show(); }, stop: function() { _inDrag = false; _animate = true; _previewDiv.hide(); _images.hide(); } })
            .mouseover(function() {
                $().stop();
                _open = true;
                _close = false;
                window.clearTimeout(_timer);
                _element = this;
                timer = setTimeout(function() {
                    if (_open && _animate) {
                        $(_element).animate(createAnimationProperties(_element, true), _settings.duration, _settings.easing);
                    }
                }, _settings.delay);
            }).mouseout(function() {
                $().stop();
                _open = false;
                _close = true;
                window.clearTimeout(_timer);
                _element = this;
                timer = setTimeout(function() {
                    if (_close && _animate) {
                        $(_element).animate(createAnimationProperties(_element, false), _settings.duration, _settings.easing);
                    }
                }, _settings.delay);
            }).mousedown(function() {
                $().stop();
                _animate = false;
            }).mouseup(function() {
                _animate = true;
            })

        return this;
    }

    function borderWidth(element) {
        var borderWidth = isNaN($(element).css("border-left-width").replace("px", "")) ? 0 : parseInt($(element).css("border-left-width"));
        return borderWidth += isNaN($(element).css("border-right-width").replace("px", "")) ? 0 : parseInt($(element).css("border-right-width"));
    }

    function borderHeight(element) {
        var borderWidth = isNaN($(element).css("border-top-width").replace("px", "")) ? 0 : parseInt($(element).css("border-top-width"));
        return borderWidth += isNaN($(element).css("border-bottom-width").replace("px", "")) ? 0 : parseInt($(element).css("border-bottom-width"));
    }

    function createImages() {
        $.each(_elements, function(i, val) {
            $("<span>").addClass("dock_image").addClass(i).appendTo(_container).data("position", i);
        });

        return _container.children();
    }

    function getDocumentHeight() {
        return $(window).height();
    }

    function getDocumentWidth() {
        return $(window).width();
    }

    function restoreDefaults(element) {
        return element.css({ left: element.data("originalLeft") + $(window).scrollLeft(), top: element.data("originalTop") + $(window).scrollTop(), width: element.data("originalWidth"), height: element.data("originalHeight") }).show();
    }

    function initialize() {
        _height = getDocumentHeight();
        _width = getDocumentWidth();
        positionImages();
        initDockers();
        initSizes();
    }

    function positionImages() {
        var top = $("span.top", _container);
        var right = $("span.right", _container);
        var bottom = $("span.bottom", _container);
        var left = $("span.left", _container);
        var dock = $("span.dock", _container);

        top.css({ "top": $(window).scrollTop() + 10, "left": _width / 2 + $(window).scrollLeft() - top.outerWidth(true) / 2 });
        bottom.css({ "top": _height + $(window).scrollTop() - bottom.outerHeight(true) - 10, "left": parseInt(top.css("left")) });
        left.add(right).css("top", _height / 2 + $(window).scrollTop() - left.outerHeight(true) / 2);
        left.css("left", Math.abs($(window).scrollLeft() + 10));
        right.css("left", _width + $(window).scrollLeft() - right.outerWidth(true) - 10);
    }

    function initDockers() {
        positionDockers();
        $.each({ position: "top", originalTop: parseInt(_elements["top"].css("top")), originalLeft: parseInt(_elements["top"].css("left")) }, function(i, val) { _elements["top"].data(i, val); });
        $.each({ position: "right", originalTop: parseInt(_elements["right"].css("top")), originalLeft: parseInt(_elements["right"].css("left")) }, function(i, val) { _elements["right"].data(i, val); });
        $.each({ position: "bottom", originalTop: parseInt(_elements["bottom"].css("top")), originalLeft: parseInt(_elements["bottom"].css("left")) }, function(i, val) { _elements["bottom"].data(i, val); });
        $.each({ position: "left", originalTop: parseInt(_elements["left"].css("top")), originalLeft: parseInt(_elements["left"].css("left")) }, function(i, val) { _elements["left"].data(i, val); });
    }

    function initSizes() {
        $.each({ position: "top", originalHeight: _elements["top"].outerHeight(true), originalwidth: _elements["top"].outerWidth(true) }, function(i, val) { _elements["top"].data(i, val); });
        $.each({ position: "right", originalHeight: _elements["right"].outerHeight(true), originalWidth: _elements["right"].outerWidth(true) }, function(i, val) { _elements["right"].data(i, val); });
        $.each({ position: "bottom", originalHeight: _elements["bottom"].outerHeight(true), originalwidth: _elements["bottom"].outerWidth(true) }, function(i, val) { _elements["bottom"].data(i, val); });
        $.each({ position: "left", originalHeight: _elements["left"].outerHeight(true), originalWidth: _elements["left"].outerWidth(true) }, function(i, val) { _elements["left"].data(i, val); });
    }

    function positionDockers() {
        _elements["top"].css({ "top": $(window).scrollTop(), "left": _width / 2 + $(window).scrollLeft() - _elements["top"].outerWidth(true) / 2 });
        _elements["right"].css({ "top": _height / 2 + $(window).scrollTop() - _elements["right"].outerHeight(true) / 2, "left": _width + $(window).scrollLeft() - _elements["right"].outerWidth(true) - borderWidth(_elements["right"]) });
        _elements["bottom"].css({ "top": _height + $(window).scrollTop() - _elements["bottom"].outerHeight(true) - borderHeight(_elements["bottom"]), "left": _width / 2 + $(window).scrollLeft() - _elements["bottom"].outerWidth(true) / 2 });
        _elements["left"].css({ "top": parseInt(_elements["right"].css("top")), "left": $(window).scrollLeft() });
    }

    function positionPreview(element) {
        var properties = { "height": "", "width": "", "top": "", "left": "" };

        switch ($(element).data("position")) {

            case "top":

                properties["top"] = $(window).scrollTop();
                properties["left"] = $(window).scrollLeft();
                properties["height"] = _settings.expansion;
                properties["width"] = _width;
                break;

            case "right":

                properties["top"] = $(window).scrollTop();
                properties["left"] = _width + $(window).scrollLeft() - _settings.expansion;
                properties["height"] = _height;
                properties["width"] = _settings.expansion;
                break;

            case "bottom":

                properties["top"] = _height + $(window).scrollTop() - _settings.expansion;
                properties["left"] = $(window).scrollLeft();
                properties["height"] = 100;
                properties["width"] = _width;
                break;

            case "left":

                properties["top"] = $(window).scrollTop();
                properties["left"] = $(window).scrollLeft();
                properties["height"] = _height;
                properties["width"] = _settings.expansion;
                break;
        }

        _previewDiv.css(properties).show();
    }

    function createAnimationProperties(element, isOpenning) {
        var jElement = $(element);
        var properties = { queue: false };

        switch (jElement.data("position")) {

            case "top":

                if (isOpenning)
                    properties["height"] = jElement.data("originalHeight") + _settings.expansion;
                else
                    properties["height"] = jElement.data("originalHeight");

                break;

            case "right":

                if (isOpenning) {
                    properties["width"] = jElement.data("originalWidth") + _settings.expansion;
                    properties["left"] = jElement.data("originalLeft") - _settings.expansion;
                }
                else {
                    properties["width"] = jElement.data("originalWidth");
                    properties["left"] = jElement.data("originalLeft");
                }

                break;

            case "bottom":

                if (isOpenning) {
                    lblMessage.val("originalTop : " + jElement.data("originalTop") + " scrollTop :" + $(window).scrollTop() + " _settings.expansion : " + _settings.expansion);
                    properties["top"] = jElement.data("originalTop") + $(window).scrollTop() - _settings.expansion;
                    properties["height"] = jElement.data("originalHeight") + _settings.expansion;
                }
                else {
                    lblMessage.val("originalTop : " + jElement.data("originalTop") + " scrollTop :" + $(window).scrollTop() + " _settings.expansion : " + _settings.expansion);
                    properties["top"] = jElement.data("originalTop") + $(window).scrollTop();
                    properties["height"] = jElement.data("originalHeight");
                }

                break;

            case "left":

                if (isOpenning)
                    properties["width"] = jElement.data("originalWidth") + _settings.expansion;
                else
                    properties["width"] = jElement.data("originalWidth");

                break;
        }

        return properties;
    }

    function create_previewDiv() {
        return $("<div>").css({ "position": "absolute", "z-Index": 10 })
        .hide()
        .addClass("transparent_div")
        .append($("<span>").css({ "width": "100%", "height": "100%", "opacity": 0.3 }).addClass("transparent"))
        .appendTo(_container);
    }
})(jQuery);
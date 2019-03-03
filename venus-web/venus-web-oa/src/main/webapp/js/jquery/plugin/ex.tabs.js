/**
 * tabs 1.0 - jQuery Plug-in
 * 
 * Licensed under the GPL:
 *   http://gplv3.fsf.org
 *
 * Copyright 2009 stworthy [ stworthy@gmail.com ] 
 */
(function($){
	
	// get the left position of the tab element
	function getTabLeftPosition(container, tab) {
		var w = 0;
		var b = true;
		$('>div.tabs-header ul.tabs li', container).each(function(){
			if (this == tab) {
				b = false;
			}
			if (b == true) {
				w += $(this).outerWidth(true);
			}
		});
		return w;
	}
	
	// get the max tabs scroll width(scope)
	function getMaxScrollWidth(container) {
		var header = $('>div.tabs-header', container);
		var tabsWidth = 0;	// all tabs width
		$('ul.tabs li', header).each(function(){
			tabsWidth += $(this).outerWidth(true);
		});
		var wrapWidth = $('.tabs-wrap', header).width();
		var padding = parseInt($('.tabs', header).css('padding-left'));
		
		return tabsWidth - wrapWidth + padding;
	}
	
	// set the tabs scrollers to show or not,
	// dependent on the tabs count and width
	function setScrollers(container) {
		var header = $('>div.tabs-header', container);
		var tabsWidth = 0;
		$('ul.tabs li', header).each(function(){
			tabsWidth += $(this).outerWidth(true);
		});
		
		if (tabsWidth > header.width()) {
			$('.tabs-scroller-left', header).css('display', 'block');
			$('.tabs-scroller-right', header).css('display', 'block');
			$('.tabs-wrap', header).addClass('tabs-scrolling');
			
			if ($.boxModel == true) {
				$('.tabs-wrap', header).css('left',2);
			} else {
				$('.tabs-wrap', header).css('left',0);
			}
			var width = header.width()
				- $('.tabs-scroller-left', header).outerWidth()
				- $('.tabs-scroller-right', header).outerWidth();
			$('.tabs-wrap', header).width(width);
			
		} else {
			$('.tabs-scroller-left', header).css('display', 'none');
			$('.tabs-scroller-right', header).css('display', 'none');
			$('.tabs-wrap', header).removeClass('tabs-scrolling').scrollLeft(0);
			$('.tabs-wrap', header).width(header.width());
			$('.tabs-wrap', header).css('left',0);
			
		}
	}
	
	// set size of the tabs container
	function setSize(container) {
		var header = $('>div.tabs-header', container);
		if ($.boxModel == true) {
			var delta = header.outerWidth(true) - header.width();
			header.width($(container).width() - delta);
		} else {
			//header.width($(container).width());
			//如果是IE浏览器，需要通过offsetWidth获取其实际宽度
			var pwidth = $(container).parent().attr( "offsetWidth" );
			header.width( pwidth );
		}
		
		setScrollers(container);
		
		var tabHeight = $('>div.tabs-header ul.tabs', container).outerHeight(true);
		var panels = $('>div.tabs-panels', container);
		//var height = parseInt($(container).css('height'));

		var height = $(container).parent().height() - 17;
		//当容器l的parent嵌套列式的frameset时，必须通过frame的id寻找到正确的frame并获取高度，容器要设置framesetOfFrameId属性。
		if(!(typeof($(container).attr("framesetOfFrameId"))=="undefined")){
    		height = $('#'+$(container).attr("framesetOfFrameId"),parent.document).outerHeight() - 17;
		}
		
		if (!isNaN(height)) {
			if ($.boxModel == true) {
				var delta = panels.outerHeight(true) - panels.height();
				panels.css('height', height - tabHeight - delta || 'auto');
			} else {
				panels.css('height', height - tabHeight);
			}
		}
			
		var width = $(container).width();
		if ($.boxModel == true) {
			var delta = panels.outerWidth(true) - panels.width();
			panels.css('width', width - delta);
		} else {
			//panels.width(width);
			//如果是IE浏览器，需要通过offsetWidth获取其实际宽度
			var pwidth = $(container).parent().attr( "offsetWidth" );
            panels.width(pwidth);
		}
		
		// resize the children tabs container
		$('div.tabs-container', container).tabs('resize');	
	}
	
	// wrap the tabs header and body
	function wrapTabs(container) {
		$(container).wrapInner('<div class="tabs-panels"/>');
		$('<div class="tabs-header">'
				+ '<div class="tabs-scroller-left"></div>'
				+ '<div class="tabs-scroller-right"></div>'
				+ '<div class="tabs-wrap">'
				+ '<ul class="tabs"></ul>'
				+ '</div>'
				+ '</div>').prependTo(container);
		
		var header = $('>div.tabs-header', container);
		
		$('>div.tabs-panels>div', container).each(function(){
			if (!$(this).attr('id')) {
				$(this).attr('id', 'gen-tabs-panel' + $.fn.tabs.defaults.idSeed++);
			}
			
			var options = {
				id: $(this).attr('id'),
				title: $(this).attr('title'),
				content: null,
				href: $(this).attr('href'),
				closable: $(this).attr('closable') == 'true',
				icon: $(this).attr('icon'),
				selected: $(this).attr('selected') == 'true',
				cache: $(this).attr('cache') == 'false' ? false : true
			};
			createTab(container, options);
		});
		
		$('.tabs-scroller-left, .tabs-scroller-right', header).hover(
			function(){$(this).addClass('tabs-scroller-over');},
			function(){$(this).removeClass('tabs-scroller-over');}
		);
	}
	
	function createTab(container, options) {
		var header = $('>div.tabs-header', container);
		var tabs = $('ul.tabs', header);
		
		var tab = $('<li></li>');
		var tab_span = $('<span></span>').html(options.title);
		var tab_a = $('<a class="tabs-inner"></a>')
				.attr('href', 'javascript:void(0)')
				.append(tab_span);
		tab.append(tab_a).appendTo(tabs);
		
		if (options.closable) {
			tab_span.addClass('tabs-closable');
			tab_a.after('<a href="javascript:void(0)" class="tabs-close"></a>');
		}
		if (options.icon) {
			tab_span.addClass('tabs-with-icon');
			tab_span.after($('<span/>').addClass('tabs-icon').addClass(options.icon));
		}
		if (options.selected) {
			tab.addClass('tabs-selected');
		}
		if (options.content) {
			$('#' + options.id).html(options.content);
		}
		
		$.data(tab[0], 'tabs.tab', {
			id: options.id,
			title: options.title,
			href: options.href,
			loaded: false,
			cache: options.cache
		});
	}
	
	function addTab(container, options) {
		options = $.extend({
			id: null,
			title: '',
			content: '',
			href: null,
			cache: true,
			icon: null,
			closable: false,
			selected: true
		}, options || {});
		
		if (options.selected) {
			$('.tabs-header .tabs-wrap .tabs li', container).removeClass('tabs-selected');
		}
		options.id = 'gen-tabs-panel' + $.fn.tabs.defaults.idSeed++;
		
		$('<div></div>').attr('id', options.id)
				.attr('title', options.title)
				.appendTo($('>div.tabs-panels', container));
		
		createTab(container, options);
	}
	
	// close a tab with special title
	function closeTab(container, title) {
		var opts = $.data(container, 'tabs').options;
		//var elem = $('>div.tabs-header li:has(a span:contains("' + title + '"))', container)[0];
		var elem;

		/*chrome浏览器中不能通过textContent匹配到
		if ($.browser.msie){
		  elem= $('>div.tabs-header li:has(a span[innerText="' + title + '"])', container)[0];
	    }else{
		  elem= $('>div.tabs-header li:has(a span[textContent="' + title + '"])', container)[0];
		}
		*/
		//contains()只作匹配查找，不够精确，包含xx的selector和包含xxabc的selector都会查到。
		//增加[innerHTML='xx'],这样将查找内容只有xx的selector。
		//http://www.cnblogs.com/leanhunter/archive/2011/10/22/2221041.html
		elem=$('>div.tabs-header li:has(a span:contains("'+title+'")[innerHTML="'+title+'"])', container)[0];
		
		if (!elem) return;
		
		var tabAttr = $.data(elem, 'tabs.tab');
		var panel = $('#' + tabAttr.id);
		
		if (opts.onClose.call(panel, tabAttr.title) == false) return;
		
		var selected = $(elem).hasClass('tabs-selected');
		$.removeData(elem, 'tabs.tab');
		$(elem).remove();
		panel.remove();
		
		setSize(container);
		if (selected) {
			selectTab(container);
		} else {
			var wrap = $('>div.tabs-header .tabs-wrap', container);
			var pos = Math.min(
					wrap.scrollLeft(),
					getMaxScrollWidth(container)
			);
			wrap.animate({scrollLeft:pos}, opts.scrollDuration);
		}
	}
	
	// active the selected tab item, if no selected item then active the first item
	function selectTab(container, title){
		if (title) {
			//var elem = $('>div.tabs-header li:has(a span:contains("' + title + '"))', container)[0];
			var elem;
			/*chrome浏览器中不能通过textContent匹配到
			if ($.browser.msie)
			  elem = $('>div.tabs-header li:has(a span[innerText="' + title + '"])', container)[0];
			else
			  elem = $('>div.tabs-header li:has(a span[textContent="' + title + '"])', container)[0];
			  */
			  
			//contains()只作匹配查找，不够精确，包含xx的selector和包含xxabc的selector都会查到。
			//增加[innerHTML='xx'],这样将查找内容只有xx的selector。
			//http://www.cnblogs.com/leanhunter/archive/2011/10/22/2221041.html
			elem=$('>div.tabs-header li:has(a span:contains("'+title+'")[innerHTML="'+title+'"])', container)[0];
			  
			if (elem) {
				$(elem).trigger('click');
			}
		} else {
		
			var tabs = $('>div.tabs-header ul.tabs', container);
			if ($('.tabs-selected', tabs).length == 0) {
				$('li:first', tabs).trigger('click');
			} else {
				$('.tabs-selected', tabs).trigger('click');
			}
		}
	}
	
	$.fn.tabs = function(options, param){
		if (typeof options == 'string') {
			switch(options) {
				case 'resize':
					return this.each(function(){
						setSize(this);
					});
				case 'add':
					return this.each(function(){
						addTab(this, param);
						$(this).tabs();
					});
				case 'close':
					return this.each(function(){
						closeTab(this, param);
					});
				case 'select':
					return this.each(function(){
						selectTab(this, param);
					});
			}
		}
		
		options = options || {};
		
		return this.each(function(){
			var state = $.data(this, 'tabs');
			var opts;
			if (state) {
				opts = $.extend(state.options, options);
				state.options = opts;
			} else {
				wrapTabs(this);
				opts = $.extend({}, $.fn.tabs.defaults, options);
				$.data(this, 'tabs', {
					options: opts
				});
			}
			
			var container = this;
			var header = $('>div.tabs-header', container);
			var tabs = $('ul.tabs', header);
			
			if (opts.plain == true) {
				header.addClass('tabs-header-plain');
			} else {
				header.removeClass('tabs-header-plain');
			}
			
			if (state) {
				$('li', tabs).unbind('.tabs');
				$('a.tabs-close', tabs).unbind('.tabs');
				$('.tabs-scroller-left', header).unbind('.tabs');
				$('.tabs-scroller-right', header).unbind('.tabs');
			}
			
			$('li', tabs).bind('click.extabs', onClickTab);
			$('a.tabs-close', tabs).bind('click.extabs', onCloseTab);
			$('.tabs-scroller-left', header).bind('click.extabs', onClickScrollLeft);
			$('.tabs-scroller-right', header).bind('click.extabs', onClickScrollRight);

			setSize(container);
			selectTab(container);
			
			function onCloseTab() {
				//var elem = $(this).parent()[0];
				//var tabAttr = $.data(elem, 'tabs.tab');
				
				var title=$(this).parent().find("a span").text();
				closeTab(container, title);
			}
            //获取上下文信息
            function getContextPath() {
                var pathName = document.location.pathname;
                var index = pathName.substr(1).indexOf("/");
                var result = pathName.substr(0,index+1);
                return result;
            }

            //增加功能：标签切换时在session中更新_function_id_
			 /**
			 * 打开新标签
			 */
            function onClickTab() {
                var tabTitle = $(this).children(".tabs-inner").children("span").text();
                var url = $('div[title='+tabTitle+']').children("iframe").attr("src");
                if ( url ){
                    var arrs = url.split( "_function_id_=" );
                    if ( 2 == arrs.length ){
                        var functionid = arrs[1];
						//getContextPath() = 'jsp', 这里重复路径了 by changming.y
						//$.get( getContextPath() + '/jsp/authority/menu/setfunctionid.jsp', { fid: functionid });
                        $.get( getContextPath() + '/authority/menu/setfunctionid.jsp', { fid: functionid });
                    }
                }
                
				$('.tabs-selected', tabs).removeClass('tabs-selected');
				$(this).addClass('tabs-selected');
				
				$('>div.tabs-panels>div', container).css('display', 'none');
				
				var wrap = $('.tabs-wrap', header);
				var leftPos = getTabLeftPosition(container, this);
				var left = leftPos - wrap.scrollLeft();
				var right = left + $(this).outerWidth();
				if (left < 0 || right > wrap.innerWidth()) {
					var pos = Math.min(
							leftPos - (wrap.width()-$(this).width()) / 2,
							getMaxScrollWidth(container)
					);
					wrap.animate({scrollLeft:pos}, opts.scrollDuration);
				}
				
				var tabAttr = $.data(this, 'tabs.tab');
				var panel = $('#' + tabAttr.id);
				panel.css('display', 'block').focus();
				$('div.tabs-container', panel).tabs('resize');
				
				if (tabAttr.href && (!tabAttr.loaded || !tabAttr.cache)) {
					panel.load(tabAttr.href, null, function(){
						opts.onLoad.apply(this, arguments);
						tabAttr.loaded = true;
					});
				}
				
				opts.onSelect.call(panel, tabAttr.title);
			}
			
			function onClickScrollLeft() {
				var wrap = $('.tabs-wrap', header);
				var pos = wrap.scrollLeft() - opts.scrollIncrement;
				wrap.animate({scrollLeft:pos}, opts.scrollDuration);
			}
			
			function onClickScrollRight() {
				var wrap = $('.tabs-wrap', header);
				var pos = Math.min(
						wrap.scrollLeft() + opts.scrollIncrement,
						getMaxScrollWidth(container)
				);
				wrap.animate({scrollLeft:pos}, opts.scrollDuration);
			}
		});
	};
	
	$.fn.tabs.defaults = {
		idSeed: 0,
		plain: false,
		scrollIncrement: 100,
		scrollDuration: 400,
		onLoad: function(){},
		onSelect: function(){},
		onClose: function(){}
	};
	
	$(function(){
		$('.tabs-container').tabs();
	});
})(jQuery);

/*
 * jQuery UI Groupbox 0.0.9
 *
 * Copyright 2012, Chad LaVigne
 * Licensed under the MIT license (http://www.opensource.org/licenses/mit-license.php) 
 *
 * http://code.google.com/p/jquery-ui-plugins/wiki/Groupbox
 *
 * Depends:
 *  jquery 1.8.2
 *	jquery.ui.core.js
 *	jquery.ui.widget.js
 */
;(function($, undefined) {
	
	$.widget('uiplugins.groupbox', {  
		options: {		
			"buttonSize": "medium",
			"buttonStyle": {},
			"cssClassList1": "",
			"cssClassList2": "",
			"height": "500",
			"idAttr": "id",
			"itemSize": "medium",
			"itemsList1": [],
			"itemsList2": [],
			"itemStyle": {},			
			"labelAttr": "name",
			"labelList1": "From",						
			"labelList2": "To",
			"selectable": true,
			"width": "300",			
		},
		_create: function() {
			var $groupbox = this.element;
			$groupbox.addClass('ui-groupbox');			
			this.$groupbox = $groupbox;			
			this.$lastSelection;
			// assign items to an object so we can quickly access items by id
			this._initItemsObject();			
			this._render();
		},
		_initItemsObject: function() {
			var opts = this.options;	
			this.itemsObject = {};
			this._addToItemsObject(opts.itemsList1);
			this._addToItemsObject(opts.itemsList2);	
		},
		_addToItemsObject: function(itemList) {
			var itemsObject = this.itemsObject;
			var opts = this.options;	
			
			for(var i = 0; i < itemList.length; i++) {
				var item = itemList[i];
				itemsObject['ui-groupbox-item-' + item[opts.idAttr]] = item;
			}	
		},
		_render: function() {
			var self = this;
			var $groupbox = this.$groupbox;
			var opts = this.options;
			var $itemsList1 = this._renderList(opts.labelList1, opts.itemsList1, opts.cssClassList1);			
			$itemsList1.addClass('group1');
			var $buttons = $('<div class="ui-groupbox-buttons"></div>');
			$addButton = $('<button class="ui-groupbox-add" type="button"><span class="ui-icon ui-icon-arrowthick-1-e" title="Add Selected">Add</span></button>').button();
			$removeButton = $('<button class="ui-groupbox-remove" type="button"><span class="ui-icon ui-icon-arrowthick-1-w" title="Remove Selected">Remove</span></button>').button();			
			$buttons.append($addButton).append('<br/>').append($removeButton);			
			$groupbox.append($buttons).after('<div class="ui-groupbox-clear"/>');
			
			var $itemsList2 = this._renderList(opts.labelList2, opts.itemsList2, opts.cssClassList2);
			$itemsList2.addClass('group2');
			$addButton.click(function() {
				self._moveSelected($itemsList1, $itemsList2);
			});
			
			$removeButton.click(function() {
				self._moveSelected($itemsList2, $itemsList1);
			});
						
			$('div.ui-groupbox-buttons button span.ui-button-text').addClass('ui-button-' + opts.buttonSize);			
			$buttons.css('top', this._getButtonTop());
			$buttons.find('button').css(opts.buttonStyle);			
			this._bindListEvents($itemsList1, $itemsList2);
			this._bindListEvents($itemsList2, $itemsList1);			
		},
		_renderList: function(label, items, cssClass) {
			var $wrapperDiv = $('<div class="ui-groupbox-list-wrapper"></div>');
			var opts = this.options;
			$wrapperDiv.append('<label class="ui-groupbox-label">' + label + '</label>');
			
			if(cssClass) {
				$wrapperDiv.addClass(cssClass);
			}	
			
			$scrollDiv = $('<div class="ui-groupbox-scroll"></div>');
			$scrollDiv.css({"width": opts.width, "height": opts.height});
			$list = $('<ul class="ui-groupbox-list"></ul>');			
			this._refreshItems($list, items);			
			$scrollDiv.append($list);
			$wrapperDiv.append($scrollDiv);				
			this.$groupbox.append($wrapperDiv);
			
			return $list;
		},
		_refreshItems: function($list, items) {
			var itemsHtml = '';
			var opts = this.options;
			
			if(items.length) {
				for(var i = 0; i < items.length; i++) {
					var item = items[i];
					var classList = item.selected ? 'ui-selected ui-state-active ui-groupbox-item ui-item-' + opts.itemSize : 'ui-state-default ui-groupbox-item ui-item-' + opts.itemSize;
					itemsHtml += '<li id="ui-groupbox-item-' + item[opts.idAttr] + '" class="' + classList + '">' + item[opts.labelAttr] + '</li>';
				}
			}
						
			$list.html(itemsHtml);	
			$list.children('li.ui-groupbox-item').css(opts.itemStyle);
		},
		_bindListEvents: function($list, $otherList) {
			var self = this;

			$list.children('li.ui-groupbox-item').each(function() {
				self._bindListItemEvents($(this), $otherList);
			});			
			
			if(this.options.selectable) {
				this._makeListSelectable($list);
			}
		},
		_bindListItemEvents: function($listItem, $otherList) {
			var self = this;
			
			$listItem.click(function(event) {
				if(!self.options['disabled']) {
					var $item = $(this);
					var $list = $item.parent('ul');
					var selected = $item.toggleClass('ui-selected ui-state-active').hasClass('ui-selected');				
					
					if(selected) {														
						if(event.shiftKey && self.$lastSelection) {
							var itemIndex = $item.index();
							var lastItemIndex = self.$lastSelection.index();
							var start = itemIndex > lastItemIndex ? lastItemIndex + 1 : itemIndex + 1;
							var end = itemIndex > lastItemIndex ? itemIndex : lastItemIndex;										
							$list.children('li').slice(start, end).addClass('ui-selected ui-state-active');										
						}
						
						self.$lastSelection = $item;
				}
				}							
			});
			
			$listItem.dblclick(function() {
				self._moveItem($(this), $otherList);				
			});
		},
		_makeListSelectable: function($list) {			
			$list.selectable({
				distance: 10,
				start: function(event, ui) {
					$(event.target).children('li.ui-selected').removeClass('ui-state-active');					
				},
				stop: function(event, ui) {
					$(event.target).children('li.ui-selected').addClass('ui-state-active');
				}
			});
		},
		_getButtonTop: function() {
			return this.$groupbox.find('div.ui-groupbox-list-wrapper').height()/2 - this.$groupbox.find('div.ui-groupbox-buttons').height()/2 + this.$groupbox.find('label.ui-groupbox-label').height()/2;
		},
		_setOption: function(option, value) {			
			var self = this;									

			switch(option) {				
				case 'width':
					this.$groupbox.find('div.ui-groupbox-scroll').width(value);					
					break;
				case 'height':
					this.$groupbox.find('div.ui-groupbox-scroll').height(value);
					this.$groupbox.find('div.ui-groupbox-buttons').css('top', this._getButtonTop());
					break;	
				case 'selectable':
					this.$groupbox.find('ul.ui-groupbox-list').each(function() {
						if(eval(value)) {
							self._makeListSelectable($(this));
						} else {
							$(this).selectable('destroy');
						}
					});					
					break;				
				case 'labelList1':
				case 'labelList2':
					this.$groupbox.find('ul.group' + option.charAt(option.length - 1)).parents('div.ui-groupbox-list-wrapper').children('label.ui-groupbox-label').text(value);
					break;				
				case 'cssClassList1':
				case 'cssClassList2':
					this.$groupbox.find('ul.group' + option.charAt(option.length - 1)).parents('div.ui-groupbox-list-wrapper').attr('class', 'ui-groupbox-list-wrapper ' + value);
					break;				
				case 'itemsList1':
				case 'itemsList2':
					var listNumber = option == 'itemsList1' ? 1 : 2;
					if(typeof value === 'string') {
						value = $.parseJSON(value);
					}					
					this.setItems(listNumber, value);
					break;
				case 'buttonSize':					
					this.$groupbox.find('div.ui-groupbox-buttons button span.ui-button-text').removeClass('ui-button-' + this.options.buttonSize).addClass('ui-button-' + value);
					this.$groupbox.find('div.ui-groupbox-buttons').css('top', this._getButtonTop());
					break;
				case 'buttonStyle':
					if(typeof value === 'string') {
						value = $.parseJSON(value);
					}					
					this.$groupbox.find('div.ui-groupbox-buttons button').css(value);					
					break;
				case 'itemSize':
					this.$groupbox.find('li.ui-groupbox-item').removeClass('ui-item-' + this.options.itemSize).addClass('ui-item-' + value);			
					break;
				case 'itemStyle':
					if(typeof value === 'string') {
						value = $.parseJSON(value);
					}					
					this.$groupbox.find('li.ui-groupbox-item').css(value);					
					break;
			}
			
			$.Widget.prototype._setOption.apply(this, arguments);
		},
		_moveItem: function($item, $toList) {	
			if(!this.options['disabled']) {
				var $fromList = $item.parent('ul');
				var removeFromList1 = $fromList.hasClass('group1');
				var fromList = removeFromList1 ? this.options.itemsList1 : this.options.itemsList2;
				var toList = removeFromList1 ? this.options.itemsList2 : this.options.itemsList1;
				var itemIndex = $item.index();
				this._trigger('beforeMove', null, {"item": fromList[itemIndex], "fromList": fromList, "toList": toList, "fromListElement": $fromList, "toListElement": $toList});				
				var removedItem = fromList.splice(itemIndex, 1)[0];
				toList.push(removedItem);				
				var $newItem = $('<li id="' + $item.attr('id') + '" class="ui-state-default ui-groupbox-item ui-item-' + this.options.itemSize + '">' + $item.html() + '</li>');	
				$newItem.css(this.options.itemStyle);
				this._bindListItemEvents($newItem, $fromList);
				$toList.append($newItem);			
				$item.remove();
				this._trigger('afterMove', null, {"item": removedItem, "fromList": fromList, "toList": toList, "fromListElement": $fromList, "toListElement": $toList});
			}			
		},		
		_moveSelected: function($fromList, $toList) {
			var self = this;
			
			$fromList.children('li.ui-state-active').each(function() {
				self._moveItem($(this), $toList);				
			});		
		},			
		getItems: function(listNumber) {
			return listNumber == 1 ? this.options.itemsList1 : this.options.itemsList2;
		},		
		setItems: function(listNumber, items) {
			this.options['itemsList' + listNumber] = items;
			var $items = this.$groupbox.find('ul.group' + listNumber);
			this._initItemsObject();
			this._refreshItems($items, items);
			var otherListNumber = listNumber == 1 ? 2 : 1;
			this._bindListEvents($items, this.$groupbox.find('ul.group' + otherListNumber));
		},				
		getSelected: function(listNumber) {			
			var items = [];
			var itemsObject = this.itemsObject;
			
			this.$groupbox.find('ul.group' + listNumber).children('li.ui-state-active').each(function() {
				items.push(itemsObject[$(this).attr('id')]);
			});
			
			return items;
		},
		setSelected: function(listNumber, items, clearSelections) {
			var $list = this.$groupbox.find('ul.group' + listNumber);
			var isIdList = typeof items[0] !== 'object';
			
			if(clearSelections) {
				$list.children('li.ui-groupbox-item').removeClass('ui-state-active');
			}
			
			for(var i = 0; i < items.length; i++) {
				var id = isIdList ? items[i] : items[i][this.options.idAttr];
				$list.children('#ui-groupbox-item-' + id).addClass('ui-state-active');
			}
		},		
		addItem: function(listNumber, item) {	
			var opts = this.options;
			var id = 'ui-groupbox-item-' + item[opts.idAttr];			
			var otherListNumber = listNumber == 1 ? 2 : 1;
			var $list = this.$groupbox.find('ul.group' + listNumber);
			var $otherList = this.$groupbox.find('ul.group' + otherListNumber);
			var list = listNumber == 1 ? opts.itemsList1 : opts.itemsList2;
			this._trigger('beforeAdd', null, {"item": item, "list": list, "listElement": $list});
			list.push(item);			
			this.itemsObject[id] = item;
			var $newItem = $('<li id="' + id + '" class="ui-state-default ui-groupbox-item ui-item-' + this.options.itemSize + '">' + item[opts.labelAttr] + '</li>');
			$newItem.css(this.options.itemStyle);
			this._bindListItemEvents($newItem, $otherList);
			$list.append($newItem);
			this._trigger('afterAdd', null, {"item": item, "list": list, "listElement": $list});					
		},
		removeItem: function(listNumber, item) {			
			var id = typeof item === 'object' ? item[this.options.idAttr] : item;
			var theItem = this.itemsObject['ui-groupbox-item-' + id];
			var $list = this.$groupbox.find('ul.group' + listNumber);
			var $item = $list.children('#ui-groupbox-item-' + id);			
			this._trigger('beforeRemove', null, {"item": theItem, "list": list, "listElement": $list});
				
			if(theItem && $item.length) {				
				var list = this.options['itemsList' + listNumber];
				list.splice($item.index(), 1);
				delete this.itemsObject['ui-groupbox-item-' + id];
				$item.remove();
				this._trigger('afterRemove', null, {"item": theItem, "list": list, "listElement": $list});
			}			
		},
		enable: function() {			
			this.$groupbox.find('ul.ui-groupbox-list').selectable('enable');
			this.$groupbox.find('div.ui-groupbox-buttons button').button('enable');
			this.$groupbox.find('div.ui-groupbox-scroll').css('overflow-y', 'scroll');
			$.Widget.prototype.enable.call(this);
			this._trigger("enable", null, this.element);
		},
		disable: function() {
			this.$groupbox.find('ul.ui-groupbox-list').selectable('disable');
			this.$groupbox.find('div.ui-groupbox-buttons button').button('disable');
			this.$groupbox.find('div.ui-groupbox-scroll').css('overflow-y', 'hidden');
			$.Widget.prototype.disable.call(this);
			this._trigger("disable", null, this.element);
		},
		destroy: function() {
			var $groupbox = this.$groupbox;			
			$groupbox.empty();
			$groupbox.siblings('div.ui-groupbox-clear').remove();
			$groupbox.removeClass('ui-groupbox');			
			
			$.Widget.prototype.destroy.call(this);		
		}
	});
})(jQuery);
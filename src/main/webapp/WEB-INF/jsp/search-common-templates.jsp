<script id="mirador-menu-item-template" type="text/x-handlebars-template">
	<a href="{{href}}" class="mirador-btn {{linkClass}} selected" role="button" aria-label="{{label}}">
		<i class="{{iconClass}} fa-lg fa-fw"></i>
		{{html}}		
	</a>
</script>

<script id="meta-data-field-template" type="text/x-handlebars-template">
	{{#if value}}
		<tr class="{{class}}">
			{{#if label}}
				<td class="metaLabel">{{label}}</td>
			{{/if}}
			<td>
				<span class="{{valueClass}}">{{{value}}}</span>
				
				{{#if note}}
					({{note}})
				{{/if}}

				{{#if lcUri}}
					<a href="{{lcUri}}" target="_blank"><img src="${pageContext.request.contextPath}/static/img/library_of_congress.png" class="lcImage"/></a>
				{{/if}}

				{{#if viafUri}}
					<a href="{{viafUri}}" target="_blank" class="viafLink">VIAF</a>
				{{/if}}
			</td>
		</tr>
	{{/if}}	
</script>

<script id="meta-data-field-search-link-template" type="text/x-handlebars-template">
	<tr class="{{class}}">
		<td class="metaLabel">{{label}}</td>
		<td>
			{{#fieldSearchLink this label=label value=value fields=fields}}{{/fieldSearchLink}}
				
			{{#if note}}
				({{note}})
			{{/if}}

			{{#if lcUri}}
				<a href="{{lcUri}}" target="_blank"><img src="${pageContext.request.contextPath}/static/img/library_of_congress.png" class="lcImage"/></a>
			{{/if}}

			{{#if viafUri}}
				<a href="{{viafUri}}" target="_blank" class="viafLink">VIAF</a>
			{{/if}}
		</td>
	</tr>		
</script>

<script id="meta-data-field-search-multiple-link-template" type="text/x-handlebars-template">
	{{#if values}}
		<tr class="{{class}}">
			<td class="metaLabel">{{label}}</td>
			<td>
				{{#fieldSearchMultipleLink this label=label values=values fieldName=fieldName}}{{/fieldSearchMultipleLink}}
				
				{{#if note}}
					({{note}})
				{{/if}}

				{{#if lcUri}}
					<a href="{{lcUri}}" target="_blank"><img src="${pageContext.request.contextPath}/static/img/library_of_congress.png" class="lcImage"/></a>
				{{/if}}

				{{#if viafUri}}
					<a href="{{viafUri}}" target="_blank" class="viafLink">VIAF</a>
				{{/if}}
			</td>
		</tr>
	{{/if}}
</script>

<script id="meta-data-link-template" type="text/x-handlebars-template">
	<tr class="wrap-overflow">
		<td class="metaLabel">{{label}}</td>
		<td>
			{{#if newWindow}}
				<a href="{{link}}" target="_blank">
					{{{linkText}}}&nbsp;<span class="glyphicon glyphicon-new-window"/>					
				</a>
			{{else}}
				<a href="{{link}}" target="">
					{{{linkText}}}
				</a>
			{{/if}}			
			
			{{#if lcUri}}
				<a href="{{lcUri}}" target="_blank"><img src="${pageContext.request.contextPath}/static/img/library_of_congress.png" class="lcImage"/></a>
			{{/if}}

			{{#if viafUri}}
				<a href="{{viafUri}}" target="_blank" class="viafLink">VIAF</a>
			{{/if}}
		</td>			
	</tr>
</script>

<script id="meta-data-order-link-template" type="text/x-handlebars-template">
	<tr>
		<td class="metaLabel">{{label}}</td>
		<td>{{preLinkText}}<a href="{{link}}" target="_blank">{{linkText}}</a></td>			
	</tr>
</script>

<script id="permalink-template" type="text/x-handlebars-template">
	<tr>
		<td class="metaLabel">{{label}}</td>
		<td>
			<a href="{{link}}/{{id}}" target="_blank">				
				{{link}}/{{id}}

				{{#if image}}
					<img src="${pageContext.request.contextPath}/static/img/{{image}}" class="{{imageClass}}"/>
				{{/if}}
			</a>
		</td>		
	</tr>
</script>

<script type="text/javascript">	

	$(function() {
		Handlebars.registerHelper('render', function(partialId, options) {
		  var selector = 'script[type="text/x-handlebars-template"]#' + partialId,
		      source = $(selector).html(),
		      html = Handlebars.compile(source)(options.hash);

			return new Handlebars.SafeString(html);
		});
		
		Handlebars.registerHelper('if_not_eq', function(a, b, opts) {
		    if(a != b) {
		        return opts.fn(this);
		    } else {
		    	return opts.inverse(this);
		    }	    	        
		});
		
		Handlebars.registerHelper('if_eq', function(a, b, opts) {
		    if(a == b) {
		    	return opts.fn(this);
		    } else {
		    	return opts.inverse(this);
		    }	        
		});
			
		Handlebars.registerHelper('fieldSearchLink', function(context, options) {
			var fields = context.fields.split(',');
			var link = '';
			
			for(var i = 0; i < fields.length; i++) {
				var fieldName = fields[i];				
				var field = context.vhmmlObject[fieldName];
				
				if(field) {
					link += i > 0 ? ', ' : '';
					link += '\'' + fieldName + '\': ';
					var fieldValue = field;
					
					if($.isArray(field)) {					
						fieldValue = field[context.index][context.searchProp];											
					} else if (field[context.searchProp] !== undefined){
						fieldValue = field[context.searchProp];
					}	
					
					// the replace call escapes any single quotes that may happen to be in the field value
					link += '\'' + fieldValue.replace(/\'/g, '\\\'') + '\'';
				}				
			}
			
			// if at least one of the fields had a value, make the link
			if(link) {								
				link = '<a href="javascript:fieldLinkSearch({' + link + '});">' + context.linkText + '</a>' 
			}
			
			return link;
		});
		
		Handlebars.registerHelper('fieldSearchMultipleLink', function(context, options) {
			var links = '';
			
			if(context.values) {
				var fieldName = context.fieldName;
				var separator = context.separator ? context.separator : ';'; 
				var values = context.values.split(separator);
				
				for(var i = 0; i < values.length; i++) {				
					if(i > 0) {
						links += '; ';
					}
					var value = values[i].trim();
					var escapedValue = value.replace(/\'/g, '\\\'');
					links += '<a href="javascript:fieldLinkSearch({\'' + fieldName + '\':\'' + escapedValue + '\'});">' + value + '</a>';	
				}	
			}			
			
			return links;
		});	
	});
</script>
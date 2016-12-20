
$(function() {	
	
	if($('#countryId').val()) {
		$('button.addListOption.cityId').show();
	}
	
	if($('#cityId').val()) {
		$('button.addListOption.repositoryId').show();
	}
	
	$('div.container').on('change', 'select.country', function() {
		var $countryList = $(this);
		var $formSection = $countryList.closest('div.formSection');
		var $cityList = $formSection.find('select.city');
		var $repoList = $formSection.find('select.repository')
		
		getDependentAuthorityListOptions($countryList, $cityList, '/readingRoom/getCities/', function() {			
			resetDependentLists([$cityList, $formSection.find('select.repository')]);
		});
		
		// if there is no country selected, we show the entire list of cities & repos 
		// if we have them (users can search by city or repo alone on Reading Room & Catalog DB home pages)
		if(!$countryList.val()) {
			showAllCitiesAndRepos($countryList);
		}
	});

	$('select.city').change(function() {
		var $cityList = $(this);
		getDependentAuthorityListOptions($cityList, $('select.repository'), '/readingRoom/getRepositories/', function() {
			resetDependentLists([$('select.repository')]);
		});			
		
	});	
	
	// if there's a saved search with a country filter, select the country & retrieve the correct cities/repos
	if(typeof savedSearch != 'undefined' && savedSearch && savedSearch.searchTerms.country) {
		// this will cause the correct city list to be fetched
		$('select.country').change();
		
		// if there was a saved search with a city filter, wait for the city list update event then select the correct city
		if(savedSearch.searchTerms.city) {
			$('select.city').on('listUpdated', function() {
				selectOptionByText($(this), savedSearch.searchTerms.city);
			});
		}
		// if there was a saved search with a repository filter, wait for the repository list update event then select the correct repository
		if(savedSearch.searchTerms.repository) {
			$('select.repository').on('listUpdated', function() {
				selectOptionByText($(this), savedSearch.searchTerms.repository);
			});			
		}
	} else {
		showAllCitiesAndRepos();
	}	
});

function resetDependentLists(dependentLists) {
	for(var i = 0; i < dependentLists.length; i++) {
		var $list = dependentLists[i];
		var defaultOption = $list.attr('data-default'); 
		
		if($list.length && $list.hasClass('updateAuthorityFields')) {
			$list.html('<option value="">' + (defaultOption ? defaultOption : '--- Select ---') + '</option>');
			$list.nextAll('button.addListOption').hide();
			var $lcUriField = $list.parents('div.form-group').next('div.form-group');
			$lcUriField.find('input.authorityUriLC').val('');
			$lcUriField.next('div.form-group').find('input.authorityUriVIAF').val('');	
		}
		
		refreshBootstrapSelect($list);		
	}
}

function getDependentAuthorityListOptions($selectList, $dependentList, url, onComplete) {
	var selectedValue =  $selectList.val();

	if(selectedValue) {
		$.ajax({
			url: contextPath + url + selectedValue,				
			success: function(newOptions) {
				if(onComplete) {
					onComplete.call();
				}
				updateAuthorityListOptions($dependentList, newOptions);	
				$dependentList.nextAll('button.addListOption').show();
			},
			error: function(response) {
				Messages.addMessage({
					message: response.responseText,
					severity: 'ERROR'
				});
			}
		});	
	}
}

function updateAuthorityListOptions($element, newOptions) {
	
	var defaultOption = $element.attr('data-default');
	var optionsHtml = '<option value="">' + (defaultOption ? defaultOption : '--- Select ---') + '</option>';

	if(newOptions && newOptions.length) {

		for(var i = 0; i < newOptions.length; i++) {
			var option = newOptions[i];						
			var lcUri = option.authorityUriLC ? option.authorityUriLC : '';
			var viafUri = option.authorityUriVIAF ? option.authorityUriVIAF : '';
			var value = option.id ? option.id : option.value;
			optionsHtml += '<option value="' + value + '" data-authority-uri-lc="' + lcUri + '" data-authority-uri-viaf="' + viafUri + '">' + option.name + '</option>';
		}
	}
	
	// we detach the element, update the options list then re-insert the element because replacing 
	// the options on large lists is very slow in chrome due to a bug introduced in version 51.0.2704.79 m
	// see http://stackoverflow.com/questions/37619590/bug-introduced-on-chrome-v51-0-2704-79-m-impossible-to-empty-large-select
	var $nextElement = $element.next();
	var $parent = $element.parent();
	$element = $element.detach().empty().html(optionsHtml);
	
	if($nextElement.length) {
		$nextElement.before($element);
	} else {
		$parent.append($element);
	}
		
	refreshBootstrapSelect($element);
	$element.trigger('listUpdated', $element.attr('name'));
}

function showAllCitiesAndRepos($countrySelect) {
	
	if(typeof citiesJson !== typeof undefined && typeof repositoriesJson !== typeof undefined) {
		$countrySelect = $countrySelect ? $countrySelect : $('select.country');
		
		$countrySelect.each(function() {
			var $form = $(this).parents('form');
			var $cityList = $form.find('select.city');
			var $repoList = $form.find('select.repository'); 
			updateAuthorityListOptions($cityList, citiesJson);
			updateAuthorityListOptions($repoList, repositoriesJson);
		});		
	}
}
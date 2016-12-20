if(!String.prototype.left) {
	String.prototype.left = function(n) {
		if (n <= 0) {
			return '';
		} else if (n > this.length) {
			return this;
		} else
			return this.substring(0, n);
	};
}

if(!String.prototype.right) {
	String.prototype.right = function(n) {
		if(n <= 0) {
			return '';
		} else if (n > this.length) {
			return this;
		} else {
			var iLen = this.length;
			return this.substring(iLen, iLen - n);
		}
	};
}

if(!String.prototype.padLeft) {
	String.prototype.padLeft = function(toLength, character) {
		var padded = this;
		while(padded.length < toLength) {
			padded = character + padded;
		}
	
		return padded;
	};
}

if(!String.prototype.contains) {
    String.prototype.contains = function(prefix) {
        return this.indexOf(prefix) > -1;
    };
}

if(!String.prototype.containsAny) {
    String.prototype.containsAny = function(arrayOfStrings) {
        for (var x in arrayOfStrings) {
            if (this.indexOf(arrayOfStrings[x]) > -1) {
                return true;
            }
        }
        return false;
    };
}

if(!String.prototype.startsWith) {
    String.prototype.startsWith = function(prefix) {
        return this.indexOf(prefix) === 0;
    };
}

if(!String.prototype.endsWith) {
    String.prototype.endsWith = function(suffix) {    	
    	if (this.length < suffix.length) {
    		return false;
    	}
 
    	return this.lastIndexOf(suffix) === this.length - suffix.length;     	
    };
}

if(!String.prototype.toProperCase) {
	String.prototype.toProperCase = function () {
	    return this.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
	};
}

;(function($, undefined) {	
	
	$.fn.padding = function(position) {
  
		var value = 0;
	
		this.each(function() {
			value = Number($(this).css('padding-' + position).replace('px', ''));
		});
	
		return value;
	};
	
	var chars = new Array();
	chars[32]=' ';
	chars[48]='0';
	chars[49]='1';
	chars[50]='2';
	chars[51]='3';
	chars[52]='4';
	chars[53]='5';
	chars[54]='6';
	chars[55]='7';
	chars[56]='8';
	chars[57]='9';
	chars[65]='a';
	chars[66]='b';
	chars[67]='c';
	chars[68]='d';
	chars[69]='e';
	chars[70]='f';
	chars[71]='g';
	chars[72]='h';
	chars[73]='i';
	chars[74]='j';
	chars[75]='k';
	chars[76]='l';
	chars[77]='m';
	chars[78]='n';
	chars[79]='o';
	chars[80]='p';
	chars[81]='q';
	chars[82]='r';
	chars[83]='s';
	chars[84]='t';
	chars[85]='u';
	chars[86]='v';
	chars[87]='w';
	chars[88]='x';
	chars[89]='y';
	chars[90]='z';
	chars[96]='0';
	chars[97]='1';
	chars[98]='2';
	chars[99]='3';
	chars[100]='4';
	chars[101]='5';
	chars[102]='6';
	chars[103]='7';
	chars[104]='8';
	chars[105]='9';
	chars[106]='*';
	chars[107]='+';
	chars[109]='-';
	chars[110]='.';
	chars[111]='/';
	chars[186]=';';
	chars[187]='=';
	chars[188]=',';
	chars[189]='-';
	chars[190]='.';
	chars[191]='/';
	chars[192]='`';
	chars[219]='[';
	chars[220]='\\';
	chars[221]=']';
	chars[222]='\'';
	
	var shiftChars = new Array();
	shiftChars[32]=' ';
	shiftChars[48]=')';
	shiftChars[49]='!';
	shiftChars[50]='@';
	shiftChars[51]='#';
	shiftChars[52]='$';
	shiftChars[53]='%';
	shiftChars[54]='^';
	shiftChars[55]='&';
	shiftChars[56]='*';
	shiftChars[57]='(';
	shiftChars[65]='A';
	shiftChars[66]='B';
	shiftChars[67]='C';
	shiftChars[68]='D';
	shiftChars[69]='E';
	shiftChars[70]='F';
	shiftChars[71]='G';
	shiftChars[72]='H';
	shiftChars[73]='I';
	shiftChars[74]='J';
	shiftChars[75]='K';
	shiftChars[76]='L';
	shiftChars[77]='M';
	shiftChars[78]='N';
	shiftChars[79]='O';
	shiftChars[80]='P';
	shiftChars[81]='Q';
	shiftChars[82]='R';
	shiftChars[83]='S';
	shiftChars[84]='T';
	shiftChars[85]='U';
	shiftChars[86]='V';
	shiftChars[87]='W';
	shiftChars[88]='X';
	shiftChars[89]='Y';
	shiftChars[90]='Z';
	shiftChars[96]='0';
	shiftChars[97]='1';
	shiftChars[98]='2';
	shiftChars[99]='3';
	shiftChars[100]='4';
	shiftChars[101]='5';
	shiftChars[102]='6';
	shiftChars[103]='7';
	shiftChars[104]='8';
	shiftChars[105]='9';
	shiftChars[106]='*';
	shiftChars[107]='+';
	shiftChars[109]='-';
	shiftChars[110]=',';
	shiftChars[111]='/';
	shiftChars[186]=':';
	shiftChars[187]='+';
	shiftChars[188]='<';
	shiftChars[189]='_';
	shiftChars[190]='>';
	shiftChars[191]='?';
	shiftChars[192]='~';
	shiftChars[219]='{';
	shiftChars[220]='|';
	shiftChars[221]='}';
	shiftChars[222]='"';
	
	$.extend($.ui.keyCode, {
		A: 65,
		NUM_LOCK: 144,					
		NUMPAD_ZERO: 96,		
		PAUSE_BREAK: 19,		
		SCROLL_LOCK: 145,
		WINDOWS_RIGHT: 92,
		// This function returns the correct character for the keyboard scan code returned by keyup and keydown events.
		// This is only necessary because for some keys like the num lock key pad numbers, the keyboard scan code that
		// is returned by keyup and keydown events is not the correct ascii code and will therefore not return the correct
		// character when passed to String.fromCharCode(), i.e. if you press '7' on the number pad the keydown event returns
		// 103 which is actually an ascii 'g' so String.fromCharCode(event.keyCode) would return 'g' instead of '7'.
		keyCode2Char: function(keyCode, shift) {
			var myChar = shift ? shiftChars[keyCode] : chars[keyCode];
			myChar = (typeof myChar == 'undefined') ? '' : myChar;
			return myChar;
		}
	});
	
	if(!$.currency) {
		$.currency = function(amount, settings) {
			return formatCurrency(amount, settings);
		};
	}	
	
	function formatCurrency(amount, settings) {
	    var bc = settings.region;
	    var currency_before = '';
	    var currency_after = '';
	    
	    if(bc == 'ALL') currency_before = 'Lek';
	    if(bc == 'ARS') currency_before = '$';
	    if(bc == 'AWG') currency_before = 'f';
	    if(bc == 'AUD') currency_before = '$';
	    if(bc == 'BSD') currency_before = '$';
	    if(bc == 'BBD') currency_before = '$';
	    if(bc == 'BYR') currency_before = 'p.';
	    if(bc == 'BZD') currency_before = 'BZ$';
	    if(bc == 'BMD') currency_before = '$';
	    if(bc == 'BOB') currency_before = '$b';
	    if(bc == 'BAM') currency_before = 'KM';
	    if(bc == 'BWP') currency_before = 'P';
	    if(bc == 'BRL') currency_before = 'R$';
	    if(bc == 'BND') currency_before = '$';
	    if(bc == 'CAD') currency_before = '$';
	    if(bc == 'KYD') currency_before = '$';
	    if(bc == 'CLP') currency_before = '$';
	    if(bc == 'CNY') currency_before = '&yen;';
	    if(bc == 'COP') currency_before = '$';
	    if(bc == 'CRC') currency_before = 'c';
	    if(bc == 'HRK') currency_before = 'kn';
	    if(bc == 'CZK') currency_before = 'Kc';
	    if(bc == 'DKK') currency_before = 'kr';
	    if(bc == 'DOP') currency_before = 'RD$';
	    if(bc == 'XCD') currency_before = '$';
	    if(bc == 'EGP') currency_before = '&pound;';
	    if(bc == 'SVC') currency_before = '$';
	    if(bc == 'EEK') currency_before = 'kr';
	    if(bc == 'EUR') currency_before = '&euro;';
	    if(bc == 'FKP') currency_before = '&pound;';
	    if(bc == 'FJD') currency_before = '$';
	    if(bc == 'GBP') currency_before = '&pound;';
	    if(bc == 'GHC') currency_before = 'c';
	    if(bc == 'GIP') currency_before = '&pound;';
	    if(bc == 'GTQ') currency_before = 'Q';
	    if(bc == 'GGP') currency_before = '&pound;';
	    if(bc == 'GYD') currency_before = '$';
	    if(bc == 'HNL') currency_before = 'L';
	    if(bc == 'HKD') currency_before = '$';
	    if(bc == 'HUF') currency_before = 'Ft';
	    if(bc == 'ISK') currency_before = 'kr';
	    if(bc == 'IDR') currency_before = 'Rp';
	    if(bc == 'IMP') currency_before = '&pound;';
	    if(bc == 'JMD') currency_before = 'J$';
	    if(bc == 'JPY') currency_before = '&yen;';
	    if(bc == 'JEP') currency_before = '&pound;';
	    if(bc == 'LVL') currency_before = 'Ls';
	    if(bc == 'LBP') currency_before = '&pound;';
	    if(bc == 'LRD') currency_before = '$';
	    if(bc == 'LTL') currency_before = 'Lt';
	    if(bc == 'MYR') currency_before = 'RM';
	    if(bc == 'MXN') currency_before = '$';
	    if(bc == 'MZN') currency_before = 'MT';
	    if(bc == 'NAD') currency_before = '$';
	    if(bc == 'ANG') currency_before = 'f';
	    if(bc == 'NZD') currency_before = '$';
	    if(bc == 'NIO') currency_before = 'C$';
	    if(bc == 'NOK') currency_before = 'kr';
	    if(bc == 'PAB') currency_before = 'B/.';
	    if(bc == 'PYG') currency_before = 'Gs';
	    if(bc == 'PEN') currency_before = 'S/.';
	    if(bc == 'PLN') currency_before = 'zl';
	    if(bc == 'RON') currency_before = 'lei';
	    if(bc == 'SHP') currency_before = '&pound;';
	    if(bc == 'SGD') currency_before = '$';
	    if(bc == 'SBD') currency_before = '$';
	    if(bc == 'SOS') currency_before = 'S';
	    if(bc == 'ZAR') currency_before = 'R';
	    if(bc == 'SEK') currency_before = 'kr';
	    if(bc == 'CHF') currency_before = 'CHF';
	    if(bc == 'SRD') currency_before = '$';
	    if(bc == 'SYP') currency_before = '&pound;';
	    if(bc == 'TWD') currency_before = 'NT$';
	    if(bc == 'TTD') currency_before = 'TT$';
	    if(bc == 'TRY') currency_before = 'TL';
	    if(bc == 'TRL') currency_before = '&pound;';
	    if(bc == 'TVD') currency_before = '$';
	    if(bc == 'GBP') currency_before = '&pound;';
	    if(bc == 'USD') currency_before = '$';
	    if(bc == 'UYU') currency_before = '$U';
	    if(bc == 'VEF') currency_before = 'Bs';
	    if(bc == 'ZWD') currency_before = 'Z$';
	    
	    if( currency_before == '' && currency_after == '' ) currency_before = '$';
	    
	    var output = '';
	    if(!settings.hidePrefix) output += currency_before;
	    output += numberFormat( amount, settings.decimals, settings.decimal, settings.thousands );
	    if(!settings.hidePostfix) output += currency_after;
	    return output;
	}
	
	// Kindly borrowed from http://phpjs.org/functions/number_format
	function numberFormat(number, decimals, dec_point, thousands_sep) {
	    number = (number + '').replace(/[^0-9+\-Ee.]/g, '');
	    var n = !isFinite(+number) ? 0 : +number,
	        prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
	        sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
	        dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
	        s = '',
	        toFixedFix = function (n, prec) {
	            var k = Math.pow(10, prec);
	            return '' + Math.round(n * k) / k;
	        };
	    // Fix for IE parseFloat(0.55).toFixed(0) = 0;
	    s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
	    if (s[0].length > 3) {
	        s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
	    }
	    if ((s[1] || '').length < prec) {
	        s[1] = s[1] || '';
	        s[1] += new Array(prec - s[1].length + 1).join('0');
	    }
	    return s.join(dec);
	}
	
	function isNumber(n) {
	    return !isNaN(parseFloat(n)) && isFinite(n);
	}
	
})(jQuery);
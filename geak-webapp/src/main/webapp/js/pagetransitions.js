var PageTransitions = (function() {

	var $main = $( '#pt-main' ),
		$pages = $main.children( 'div.pt-page' ),
		animcursor = 1,
		pagesCount = $pages.length,
		current = 0,
		isAnimating = false,
		endCurrPage = false,
		endNextPage = false,
		// animation end event name
		animEndEventName = 'animationend',
		// support css animations
		support = true;
	
	function init() {

		$pages.each( function() {
			var $page = $( this );
			$page.data( 'originalClassList', $page.attr( 'class' ) );
		} );

		$pages.eq( current ).addClass( 'pt-page-current' );

	}

	function gotoPage(index) {
		if(index == current) { return false; }
		if( isAnimating ) { return false; }
		isAnimating = true;

		var $currPage = $pages.eq( current );
		var $nextPage = $pages.eq( index ).addClass( 'pt-page-current' );
		var	outClass = '', inClass = '';
		if(current < index) {
			outClass = 'pt-page-moveToLeft';
			inClass = 'pt-page-moveFromRight';
		} else {
			outClass = 'pt-page-moveToRight';
			inClass = 'pt-page-moveFromLeft';
		}
		current = index;

		$currPage.addClass( outClass ).on( animEndEventName, function() {
			$currPage.off( animEndEventName );
			endCurrPage = true;
			if( endNextPage ) {
				onEndAnimation( $currPage, $nextPage );
			}
		} );

		$nextPage.addClass( inClass ).on( animEndEventName, function() {
			$nextPage.off( animEndEventName );
			endNextPage = true;
			if( endCurrPage ) {
				onEndAnimation( $currPage, $nextPage );
			}
		} );

		if( !support ) {
			onEndAnimation( $currPage, $nextPage );
		}
	}

	
	function onEndAnimation( $outpage, $inpage ) {
		endCurrPage = false;
		endNextPage = false;
		resetPage( $outpage, $inpage );
		isAnimating = false;
	}

	function resetPage( $outpage, $inpage ) {
		$outpage.attr( 'class', $outpage.data( 'originalClassList' ) );
		$inpage.attr( 'class', $inpage.data( 'originalClassList' ) + ' pt-page-current' );
	}

	init();

	return { 
		init : init,
		gotoPage : gotoPage,
	};

})();
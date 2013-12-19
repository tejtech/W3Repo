$(function(){
  //OPENS SEARCH DROPDOWN
  $(".js-search-input-open").click(openSearchWindow);
  function openSearchWindow () {
    $(".nav-search").addClass("js-highlight");
    var inputContainer = $(".js-search-dropdown");
    var input = $(".js-search-input");
    inputContainer.addClass("js-show");

    //FOCUSES SEARCH INPUT ON OPEN
    setTimeout(function() {
      input.focus();
    }, 100);

    //CLOSES SEARCH DROPDOWN
    $(".body--container, .js-search-input-close").click(function() {
      inputContainer.removeClass("js-show");
      $(".nav-search").removeClass("js-highlight");
      $("#scrim").removeClass("js-show");
    });
  };


  //AUTO OPENS SEARCH DROPDOWN ON SEARCH VIEW AND 
/*
  if (window.location.pathname == "/search") {
    $(".nav-search").addClass("js-highlight");
    $(".js-search-dropdown").addClass("js-show no-animation");

    //PREPOPULATES INPUT WITH SEARCH QUERY
    var searchQuery = decodeURIComponent(window.location.search.substr(3).replace(/\+/g," ")); 
    $(".js-search-input").val(searchQuery);

    //CLOSES SEARCH DROPDOWN
    $(".js-search-input-close").click(function() {
      $(".js-search-dropdown").removeClass("js-show no-animation");
      $(".nav-search").removeClass("js-highlight");
    });
  };
*/
});

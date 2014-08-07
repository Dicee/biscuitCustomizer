$(document).ready(function() {
			
	$('#connect').click(function() {
		$('nav').addClass('form-active');
		$('#email').focus();
	});
			
	$('#btnquit').click(function() {
		$('nav').removeClass('form-active');
	});		
								
	$('#titres h2').css("opacity","0").each(function(i) {
		$(this).delay(i*1000+500).fadeTo(1000,1);
	});

	$("#mdp2").keyup(checkPasswordMatch);
	
	function checkPasswordMatch() {
		var mdp1 = $('#mdp1').val();
		var mdp2 = $('#mdp2').val();
		$('.check').hide();
		if (mdp2 != '') {
			if (mdp1 != mdp2) {
				$("#mdpnotok").show();
			} else {
				$("#mdpok").show();
			}
		}
	}
	
	jQuery(function(){
        $("#submit").click(function(){
		var hasError = false;
		var pseudo = $('#pseudo').val();
		var email = $('#email2').val();
		var mdp1 = $('#mdp1').val();
		var mdp2 = $('#mdp2').val();
		$('.check').hide();
		if (pseudo == '') {
            $('#pseudonotok').show();
			$('#pseudo').focus();
            hasError = true;
        } else {
			$('#pseudook').show();
		}
		if (email == '') {
			$('#emailnotok').show();
            hasError = true;
		} else {
			$('#emailok').show();
		}
        if (mdp1 == '' ) {
			$('#mdp1notok').show();
            hasError = true;
        } else {
			$('#mdp1ok').show();
		}
		if (mdp2 == '' || mdp1 != mdp2) {
			$('#mdpnotok').show();
            hasError = true;
        } else {
			$('#mdpok').show();
		}
        if(hasError) {return false;}
		}); 
	});
	
});
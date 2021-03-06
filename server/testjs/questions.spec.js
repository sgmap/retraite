'use strict';

describe('questions', function () {

	beforeEach(function () {
		jasmine.getFixtures().fixturesPath = 'base/testjs/spec/javascripts/fixtures';
	});
	
    describe('mandatory', function () {
        
    	beforeEach(function () {
    		loadFixtures('questions_mandatory.html'); 
    		RetraiteQuestions.initJquery(); 
    	});

        it('should be disabled initialy', function () {
        	expectNextButtonIsDisabled();
        });
        
        it('should be enabled if question is checked', function (done) {
        	
        	// NDLA : le 'trigger click' devrait peut-être être après le 'on event' ? ...
        	
        	$('input[value=INDEP_AVANT_73]').trigger( "click" );
    		
        	$(document).on('Retraite:questions:diplayUpdated', function() {
    			// Il faut se désabonner de l'évènement pour éviter le mélange entre chaque TU
    			$(document).off('Retraite:questions:diplayUpdated');
    			expectNextButtonIsEnabled(); 
    			expect($("input#reponseJsonStr").val()).toEqual("[\"INDEP_AVANT_73\"]");
    			done();
    		}); 
        });
        
    });
    
    describe('optionnal', function () {
    	
    	beforeEach(function () {
    		loadFixtures('questions_optionnal.html'); 
    		RetraiteQuestions.initJquery(); 
    	});
    	
    	it('should be disabled initialy', function () {
    		expectNextButtonIsDisabled();
    		//expectNextButtonHasText("Aucune de ces situations");
    	});
    	
        it('should be disabled if not all questions are checked', function (done) {
    		
        	$('[value=INDEP_AVANT_73]').trigger( "click" );
        	$('[value=INVALIDITE_RSI]').trigger( "click" );
    		
    		$(document).on('Retraite:questions:diplayUpdated', function() {
    			// Il faut se désabonner de l'évènement pour éviter le mélange entre chaque TU
    			$(document).off('Retraite:questions:diplayUpdated');
    			expectNextButtonIsDisabled();
    			done();
    		}); 
    	});
    	
        it('should be enabled if all questions are checked', function (done) {
    		
        	$('[value=INDEP_AVANT_73]').trigger( "click" );
        	$('[value=INVALIDITE_RSI]').trigger( "click" );
    		$('[value=PENIBILITE]').trigger( "click" );
    		
    		$(document).on('Retraite:questions:diplayUpdated', function() {
    			// Il faut se désabonner de l'évènement pour éviter le mélange entre chaque TU
    			$(document).off('Retraite:questions:diplayUpdated');
    			expectNextButtonIsEnabled(); 
    			expectNextButtonHasText("Étape suivante");
    			expect($("input#reponseJsonStr").val()).toEqual("[\"INDEP_AVANT_73\",\"INVALIDITE_RSI\",\"PENIBILITE\"]");
    			done();
    		}); 
    	});
    });
    
    function expectNextButtonIsDisabled() {
    	expect($(".btn-next").attr("disabled")).toEqual("disabled");
    }

    function expectNextButtonIsEnabled() {
    	expect($(".btn-next").attr("disabled")).toBeUndefined(); 
    }
    
    function expectNextButtonHasText(expectedText) {
    	expect($(".btn-next").text()).toEqual(expectedText); 
    }
    
});

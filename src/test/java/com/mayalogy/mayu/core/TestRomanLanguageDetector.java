package com.mayalogy.mayu.core;


import junit.framework.TestCase;

public class TestRomanLanguageDetector extends TestCase {

	public void testIsEnglish() {
		assertTrue(RomanLanguageDetector.isEnglish("I eat chips."));
		assertTrue(RomanLanguageDetector.isEnglish("I eat potato chips on Tuesday."));
		assertTrue(!RomanLanguageDetector.isEnglish("Je voudrais un coca."));
		assertTrue(!RomanLanguageDetector.isEnglish("Hola mi amigo megan ques pasa."));
		assertTrue(!RomanLanguageDetector.isEnglish("Tera sir dha vich tutti jumia pya"));
		assertTrue(RomanLanguageDetector.isEnglish("My eyes have seen the glory of the coming of the lord."));
	}
	
    public void testIsRoman() {
        assertTrue(RomanLanguageDetector.isRoman("hello banana"));
        assertTrue(RomanLanguageDetector.isRoman("L'université"));
        assertTrue(RomanLanguageDetector.isRoman("institución"));
        assertTrue(RomanLanguageDetector.isRoman("parkovišti u technické"));
        
        assertTrue(!RomanLanguageDetector.isRoman("πρωθυπουργός"));
        assertTrue(!RomanLanguageDetector.isRoman("搜狐俄罗斯总理普"));
        assertTrue(!RomanLanguageDetector.isRoman("東京電力が電気料金を早ければ２０１２"));
        assertTrue(!RomanLanguageDetector.isRoman("民進黨副總統參選人蘇嘉全的"));
        assertTrue(!RomanLanguageDetector.isRoman("ê°‘ìžê¸°ë“¤ë¦¬ëŠ”ì˜ì–´ ë¦¬ìŠ¤ë‹ì™•êµ­"));
    }
}

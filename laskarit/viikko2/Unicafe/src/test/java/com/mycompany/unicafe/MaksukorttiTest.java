package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void alkuSaldoOikein() {
        assertEquals(10,kortti.saldo());      
    }
    
    @Test
    public void rahanLatausToimii() {
        kortti.lataaRahaa(45);
        assertEquals(55,kortti.saldo());      
    }
    
    @Test
    public void saldoVaheneeKunRahaRiittaa() {
        kortti.otaRahaa(7);
        assertEquals(3,kortti.saldo());      
    }
    
    @Test
    public void saldoEiVaheneKunRahaEiRiita() {
        kortti.otaRahaa(45);
        assertEquals(10,kortti.saldo());      
    }
    
    @Test
    public void metodiPalauttaaTrueKunRahaRiittaa() {
        assertTrue(kortti.otaRahaa(10));      
    }
    
    @Test
    public void metodiPalauttaaFalseKunRahaEiRiita() {
        assertFalse(kortti.otaRahaa(11));      
    }
    
    @Test
    public void toStringToimiiOikein() {
        assertEquals("saldo: 0.10",kortti.toString());      
    }
}

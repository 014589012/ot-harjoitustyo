/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.unicafe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kwkalle
 */
public class KassapaateTest {
    
    Kassapaate kp;
    Maksukortti k2;
    
    @Before
    public void setUp() {
        kp = new Kassapaate();
        k2 = new Maksukortti(500);
    }
    
    @Test
    public void luotuKassapaateOikein() {
        assertEquals("100000:0",kp.kassassaRahaa()+":"+(kp.edullisiaLounaitaMyyty()+kp.maukkaitaLounaitaMyyty()));      
    }
    
    @Test
    public void kateisostoEdullinenToimii() {
        int c=kp.syoEdullisesti(500);
        assertEquals("100240:260:1",kp.kassassaRahaa()+":"+c+":"+(kp.edullisiaLounaitaMyyty()));      
    }
    
    @Test
    public void kateisostoMaukkaastiToimii() {
        int c=kp.syoMaukkaasti(550);
        assertEquals("100400:150:1",kp.kassassaRahaa()+":"+c+":"+(kp.maukkaitaLounaitaMyyty()));      
    }
    
    @Test
    public void kateisostoEdullisestiKunRahaEiRiitä() {
        int c=kp.syoEdullisesti(150);
        assertEquals("100000:150:0",kp.kassassaRahaa()+":"+c+":"+(kp.edullisiaLounaitaMyyty()+kp.maukkaitaLounaitaMyyty()));      
    }
    
    @Test
    public void kateisostoMaukkaastiKunRahaEiRiitä() {
        int c=kp.syoMaukkaasti(350);
        assertEquals("100000:350:0",kp.kassassaRahaa()+":"+c+":"+(kp.edullisiaLounaitaMyyty()+kp.maukkaitaLounaitaMyyty()));      
    }
    
    @Test
    public void korttiostoEdullinenPalauttaaTrueKunRahaRiittaa() {
        assertTrue(kp.syoEdullisesti(k2));      
    }
    
    @Test
    public void korttiostoEdullinenPalauttaaFalseKunRahaEiRiita() {
        k2.otaRahaa(400);
        assertFalse(kp.syoEdullisesti(k2));      
    }
    
    @Test
    public void korttiostoMaukkaastiPalauttaaTrueKunRahaRiittaa() {
        assertTrue(kp.syoMaukkaasti(k2));      
    }
    
    @Test
    public void korttiostoMaukkaastiPalauttaaFalseKunRahaEiRiita() {
        k2.otaRahaa(150);
        assertFalse(kp.syoMaukkaasti(k2));      
    }
    
    @Test
    public void korttiostoEdullinenToimii() {
        kp.syoEdullisesti(k2);
        assertEquals("100000:260:1",kp.kassassaRahaa()+":"+k2.saldo()+":"+kp.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void korttiostoMaukkaastiToimii() {
        kp.syoMaukkaasti(k2);
        assertEquals("100000:100:1",kp.kassassaRahaa()+":"+k2.saldo()+":"+kp.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void korttiostoEdullisestiKunRahaEiRiitä() {
        k2.otaRahaa(400);
        kp.syoEdullisesti(k2);
        assertEquals("100000:100:0",kp.kassassaRahaa()+":"+k2.saldo()+":"+(kp.edullisiaLounaitaMyyty()+kp.maukkaitaLounaitaMyyty()));      
    }
    
    @Test
    public void korttiostoMaukkaastiKunRahaEiRiitä() {
        k2.otaRahaa(150);
        kp.syoMaukkaasti(k2);
        assertEquals("100000:350:0",kp.kassassaRahaa()+":"+k2.saldo()+":"+(kp.edullisiaLounaitaMyyty()+kp.maukkaitaLounaitaMyyty()));      
    }
    
    @Test
    public void lataaRahaaKortilleToimii() {
        kp.lataaRahaaKortille(k2,50);
        assertEquals("100050:550",kp.kassassaRahaa()+":"+k2.saldo());      
    }
    
}


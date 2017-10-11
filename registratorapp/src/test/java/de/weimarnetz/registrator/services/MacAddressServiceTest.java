package de.weimarnetz.registrator.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import de.weimarnetz.registrator.exceptions.InvalidMacAddressException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MacAddressServiceTest {

    @InjectMocks
    private MacAddressService macAddressService;

    @Test
    public void validateValidMacAddress() throws Exception {
        // when
        boolean validMacAddress = macAddressService.isValidMacAddress("02:ca:ff:ee:ba:be");

        // then
        assertThat(validMacAddress).isTrue();
    }

    @Test
    public void validateValidUpperCaseMacAddress() throws Exception {
        // when
        boolean validMacAddress = macAddressService.isValidMacAddress("02:CA:FF:EE:BA:BE");

        // then
        assertThat(validMacAddress).isTrue();
    }

    @Test
    public void validateInvalidString() throws Exception {
        // when
        boolean validMacAddress = macAddressService.isValidMacAddress("02caffeebabe");

        // then
        assertThat(validMacAddress).isFalse();
    }

    @Test
    public void validateInValidMacAddress() throws Exception {
        // when
        boolean validMacAddress = macAddressService.isValidMacAddress("02:ka:ff:ee:ba:be");

        // then
        assertThat(validMacAddress).isFalse();
    }

    @Test
    public void normalizeValidMacAddress() throws Exception {
        // when
        String normalizeMacAddress = macAddressService.normalizeMacAddress("02:ca:ff:ee:ba:be");

        // then
        assertThat(normalizeMacAddress).isEqualTo("02caffeebabe");
    }

    @Test
    public void normalizeValidMacAddressWithHyphens() throws Exception {
        // when
        String normalizeMacAddress = macAddressService.normalizeMacAddress("02-ca-ff-ee-ba-be");

        // then
        assertThat(normalizeMacAddress).isEqualTo("02caffeebabe");
    }

    @Test
    public void normalizeValidMacAddressWithHyphensAndUppercase() throws Exception {
        // when
        String normalizeMacAddress = macAddressService.normalizeMacAddress("02-CA-FF-EE-BA-BE");

        // then
        assertThat(normalizeMacAddress).isEqualTo("02caffeebabe");
    }

    @Test(expected = InvalidMacAddressException.class)
    public void normalizeInvalidMacAddress() throws Exception {
        // when
        macAddressService.normalizeMacAddress("02-ka-ff-ee-ba-be");

    }

}
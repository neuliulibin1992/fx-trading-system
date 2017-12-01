import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('FxRate e2e test', () => {

    let navBarPage: NavBarPage;
    let fxRateDialogPage: FxRateDialogPage;
    let fxRateComponentsPage: FxRateComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load FxRates', () => {
        navBarPage.goToEntity('fx-rate');
        fxRateComponentsPage = new FxRateComponentsPage();
        expect(fxRateComponentsPage.getTitle()).toMatch(/fxgatewayApp.fxRate.home.title/);

    });

    it('should load create FxRate dialog', () => {
        fxRateComponentsPage.clickOnCreateButton();
        fxRateDialogPage = new FxRateDialogPage();
        expect(fxRateDialogPage.getModalTitle()).toMatch(/fxgatewayApp.fxRate.home.createOrEditLabel/);
        fxRateDialogPage.close();
    });

    it('should create and save FxRates', () => {
        fxRateComponentsPage.clickOnCreateButton();
        fxRateDialogPage.setExtractTimeInput(12310020012301);
        expect(fxRateDialogPage.getExtractTimeInput()).toMatch('2001-12-31T02:30');
        fxRateDialogPage.setArrivalDateInput('2000-12-31');
        expect(fxRateDialogPage.getArrivalDateInput()).toMatch('2000-12-31');
        fxRateDialogPage.setCurrencyQuoteInput('currencyQuote');
        expect(fxRateDialogPage.getCurrencyQuoteInput()).toMatch('currencyQuote');
        fxRateDialogPage.setCurrencyBaseCodeInput('currencyBaseCode');
        expect(fxRateDialogPage.getCurrencyBaseCodeInput()).toMatch('currencyBaseCode');
        fxRateDialogPage.setCurrencyNonBaseCodeInput('currencyNonBaseCode');
        expect(fxRateDialogPage.getCurrencyNonBaseCodeInput()).toMatch('currencyNonBaseCode');
        fxRateDialogPage.setBidPriceInput('5');
        expect(fxRateDialogPage.getBidPriceInput()).toMatch('5');
        fxRateDialogPage.setAskPriceInput('5');
        expect(fxRateDialogPage.getAskPriceInput()).toMatch('5');
        fxRateDialogPage.setMidPriceInput('5');
        expect(fxRateDialogPage.getMidPriceInput()).toMatch('5');
        fxRateDialogPage.save();
        expect(fxRateDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class FxRateComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-fx-rate div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class FxRateDialogPage {
    modalTitle = element(by.css('h4#myFxRateLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    extractTimeInput = element(by.css('input#field_extractTime'));
    arrivalDateInput = element(by.css('input#field_arrivalDate'));
    currencyQuoteInput = element(by.css('input#field_currencyQuote'));
    currencyBaseCodeInput = element(by.css('input#field_currencyBaseCode'));
    currencyNonBaseCodeInput = element(by.css('input#field_currencyNonBaseCode'));
    bidPriceInput = element(by.css('input#field_bidPrice'));
    askPriceInput = element(by.css('input#field_askPrice'));
    midPriceInput = element(by.css('input#field_midPrice'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setExtractTimeInput = function (extractTime) {
        this.extractTimeInput.sendKeys(extractTime);
    }

    getExtractTimeInput = function () {
        return this.extractTimeInput.getAttribute('value');
    }

    setArrivalDateInput = function (arrivalDate) {
        this.arrivalDateInput.sendKeys(arrivalDate);
    }

    getArrivalDateInput = function () {
        return this.arrivalDateInput.getAttribute('value');
    }

    setCurrencyQuoteInput = function (currencyQuote) {
        this.currencyQuoteInput.sendKeys(currencyQuote);
    }

    getCurrencyQuoteInput = function () {
        return this.currencyQuoteInput.getAttribute('value');
    }

    setCurrencyBaseCodeInput = function (currencyBaseCode) {
        this.currencyBaseCodeInput.sendKeys(currencyBaseCode);
    }

    getCurrencyBaseCodeInput = function () {
        return this.currencyBaseCodeInput.getAttribute('value');
    }

    setCurrencyNonBaseCodeInput = function (currencyNonBaseCode) {
        this.currencyNonBaseCodeInput.sendKeys(currencyNonBaseCode);
    }

    getCurrencyNonBaseCodeInput = function () {
        return this.currencyNonBaseCodeInput.getAttribute('value');
    }

    setBidPriceInput = function (bidPrice) {
        this.bidPriceInput.sendKeys(bidPrice);
    }

    getBidPriceInput = function () {
        return this.bidPriceInput.getAttribute('value');
    }

    setAskPriceInput = function (askPrice) {
        this.askPriceInput.sendKeys(askPrice);
    }

    getAskPriceInput = function () {
        return this.askPriceInput.getAttribute('value');
    }

    setMidPriceInput = function (midPrice) {
        this.midPriceInput.sendKeys(midPrice);
    }

    getMidPriceInput = function () {
        return this.midPriceInput.getAttribute('value');
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}

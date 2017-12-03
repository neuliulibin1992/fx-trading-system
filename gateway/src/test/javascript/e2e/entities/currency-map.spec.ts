import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('CurrencyMap e2e test', () => {

    let navBarPage: NavBarPage;
    let currencyMapDialogPage: CurrencyMapDialogPage;
    let currencyMapComponentsPage: CurrencyMapComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load CurrencyMaps', () => {
        navBarPage.goToEntity('currency-map');
        currencyMapComponentsPage = new CurrencyMapComponentsPage();
        expect(currencyMapComponentsPage.getTitle()).toMatch(/fxgatewayApp.currencyMap.home.title/);

    });

    it('should load create CurrencyMap dialog', () => {
        currencyMapComponentsPage.clickOnCreateButton();
        currencyMapDialogPage = new CurrencyMapDialogPage();
        expect(currencyMapDialogPage.getModalTitle()).toMatch(/fxgatewayApp.currencyMap.home.createOrEditLabel/);
        currencyMapDialogPage.close();
    });

    it('should create and save CurrencyMaps', () => {
        currencyMapComponentsPage.clickOnCreateButton();
        currencyMapDialogPage.setCurrencyQuoteInput('currencyQuote');
        expect(currencyMapDialogPage.getCurrencyQuoteInput()).toMatch('currencyQuote');
        currencyMapDialogPage.setCurrencyBaseCodeInput('currencyBaseCode');
        expect(currencyMapDialogPage.getCurrencyBaseCodeInput()).toMatch('currencyBaseCode');
        currencyMapDialogPage.setCurrencyNonBaseCodeInput('currencyNonBaseCode');
        expect(currencyMapDialogPage.getCurrencyNonBaseCodeInput()).toMatch('currencyNonBaseCode');
        currencyMapDialogPage.providedBySelectLastOption();
        currencyMapDialogPage.save();
        expect(currencyMapDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CurrencyMapComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-currency-map div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CurrencyMapDialogPage {
    modalTitle = element(by.css('h4#myCurrencyMapLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    currencyQuoteInput = element(by.css('input#field_currencyQuote'));
    currencyBaseCodeInput = element(by.css('input#field_currencyBaseCode'));
    currencyNonBaseCodeInput = element(by.css('input#field_currencyNonBaseCode'));
    providedBySelect = element(by.css('select#field_providedBy'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
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

    setProvidedBySelect = function (providedBy) {
        this.providedBySelect.sendKeys(providedBy);
    }

    getProvidedBySelect = function () {
        return this.providedBySelect.element(by.css('option:checked')).getText();
    }

    providedBySelectLastOption = function () {
        this.providedBySelect.all(by.tagName('option')).last().click();
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

import { element, by, ElementFinder } from 'protractor';

export class GiftComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-gift div table .btn-danger'));
    title = element.all(by.css('jhi-gift div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class GiftUpdatePage {
    pageTitle = element(by.id('jhi-gift-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nameInput = element(by.id('field_name'));
    descriptionInput = element(by.id('field_description'));
    urlInput = element(by.id('field_url'));
    giftListSelect = element(by.id('field_giftList'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNameInput(name) {
        await this.nameInput.sendKeys(name);
    }

    async getNameInput() {
        return this.nameInput.getAttribute('value');
    }

    async setDescriptionInput(description) {
        await this.descriptionInput.sendKeys(description);
    }

    async getDescriptionInput() {
        return this.descriptionInput.getAttribute('value');
    }

    async setUrlInput(url) {
        await this.urlInput.sendKeys(url);
    }

    async getUrlInput() {
        return this.urlInput.getAttribute('value');
    }

    async giftListSelectLastOption() {
        await this.giftListSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async giftListSelectOption(option) {
        await this.giftListSelect.sendKeys(option);
    }

    getGiftListSelect(): ElementFinder {
        return this.giftListSelect;
    }

    async getGiftListSelectedOption() {
        return this.giftListSelect.element(by.css('option:checked')).getText();
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class GiftDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-gift-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-gift'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}

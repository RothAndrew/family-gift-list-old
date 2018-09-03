import { element, by, ElementFinder } from 'protractor';

export class FamilyComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-family div table .btn-danger'));
    title = element.all(by.css('jhi-family div h2#page-heading span')).first();

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

export class FamilyUpdatePage {
    pageTitle = element(by.id('jhi-family-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nameInput = element(by.id('field_name'));
    membersSelect = element(by.id('field_members'));
    ownersSelect = element(by.id('field_owners'));
    adminsSelect = element(by.id('field_admins'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNameInput(name) {
        await this.nameInput.sendKeys(name);
    }

    async getNameInput() {
        return this.nameInput.getAttribute('value');
    }

    async membersSelectLastOption() {
        await this.membersSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async membersSelectOption(option) {
        await this.membersSelect.sendKeys(option);
    }

    getMembersSelect(): ElementFinder {
        return this.membersSelect;
    }

    async getMembersSelectedOption() {
        return this.membersSelect.element(by.css('option:checked')).getText();
    }

    async ownersSelectLastOption() {
        await this.ownersSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async ownersSelectOption(option) {
        await this.ownersSelect.sendKeys(option);
    }

    getOwnersSelect(): ElementFinder {
        return this.ownersSelect;
    }

    async getOwnersSelectedOption() {
        return this.ownersSelect.element(by.css('option:checked')).getText();
    }

    async adminsSelectLastOption() {
        await this.adminsSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async adminsSelectOption(option) {
        await this.adminsSelect.sendKeys(option);
    }

    getAdminsSelect(): ElementFinder {
        return this.adminsSelect;
    }

    async getAdminsSelectedOption() {
        return this.adminsSelect.element(by.css('option:checked')).getText();
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

export class FamilyDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-family-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-family'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}

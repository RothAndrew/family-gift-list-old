import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { GiftComponentsPage, GiftDeleteDialog, GiftUpdatePage } from './gift.page-object';

describe('Gift e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let giftUpdatePage: GiftUpdatePage;
    let giftComponentsPage: GiftComponentsPage;
    let giftDeleteDialog: GiftDeleteDialog;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Gifts', async () => {
        await navBarPage.goToEntity('gift');
        giftComponentsPage = new GiftComponentsPage();
        expect(await giftComponentsPage.getTitle()).toMatch(/fglgatewayApp.gift.home.title/);
    });

    it('should load create Gift page', async () => {
        await giftComponentsPage.clickOnCreateButton();
        giftUpdatePage = new GiftUpdatePage();
        expect(await giftUpdatePage.getPageTitle()).toMatch(/fglgatewayApp.gift.home.createOrEditLabel/);
        await giftUpdatePage.cancel();
    });

    it('should create and save Gifts', async () => {
        await giftComponentsPage.clickOnCreateButton();
        await giftUpdatePage.setNameInput('name');
        expect(await giftUpdatePage.getNameInput()).toMatch('name');
        await giftUpdatePage.setDescriptionInput('description');
        expect(await giftUpdatePage.getDescriptionInput()).toMatch('description');
        await giftUpdatePage.setUrlInput('url');
        expect(await giftUpdatePage.getUrlInput()).toMatch('url');
        await giftUpdatePage.giftListSelectLastOption();
        await giftUpdatePage.save();
        expect(await giftUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    it('should delete last Gift', async () => {
        const nbButtonsBeforeDelete = await giftComponentsPage.countDeleteButtons();
        await giftComponentsPage.clickOnLastDeleteButton();

        giftDeleteDialog = new GiftDeleteDialog();
        expect(await giftDeleteDialog.getDialogTitle()).toMatch(/fglgatewayApp.gift.delete.question/);
        await giftDeleteDialog.clickOnConfirmButton();

        expect(await giftComponentsPage.countDeleteButtons()).toBe(nbButtonsBeforeDelete - 1);
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});

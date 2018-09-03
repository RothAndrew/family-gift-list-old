import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { GiftListComponentsPage, GiftListDeleteDialog, GiftListUpdatePage } from './gift-list.page-object';

describe('GiftList e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let giftListUpdatePage: GiftListUpdatePage;
    let giftListComponentsPage: GiftListComponentsPage;
    let giftListDeleteDialog: GiftListDeleteDialog;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load GiftLists', async () => {
        await navBarPage.goToEntity('gift-list');
        giftListComponentsPage = new GiftListComponentsPage();
        expect(await giftListComponentsPage.getTitle()).toMatch(/fglgatewayApp.giftList.home.title/);
    });

    it('should load create GiftList page', async () => {
        await giftListComponentsPage.clickOnCreateButton();
        giftListUpdatePage = new GiftListUpdatePage();
        expect(await giftListUpdatePage.getPageTitle()).toMatch(/fglgatewayApp.giftList.home.createOrEditLabel/);
        await giftListUpdatePage.cancel();
    });

    it('should create and save GiftLists', async () => {
        await giftListComponentsPage.clickOnCreateButton();
        await giftListUpdatePage.userSelectLastOption();
        await giftListUpdatePage.save();
        expect(await giftListUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    it('should delete last GiftList', async () => {
        const nbButtonsBeforeDelete = await giftListComponentsPage.countDeleteButtons();
        await giftListComponentsPage.clickOnLastDeleteButton();

        giftListDeleteDialog = new GiftListDeleteDialog();
        expect(await giftListDeleteDialog.getDialogTitle()).toMatch(/fglgatewayApp.giftList.delete.question/);
        await giftListDeleteDialog.clickOnConfirmButton();

        expect(await giftListComponentsPage.countDeleteButtons()).toBe(nbButtonsBeforeDelete - 1);
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});

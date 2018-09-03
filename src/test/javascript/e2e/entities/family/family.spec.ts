import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { FamilyComponentsPage, FamilyDeleteDialog, FamilyUpdatePage } from './family.page-object';

describe('Family e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let familyUpdatePage: FamilyUpdatePage;
    let familyComponentsPage: FamilyComponentsPage;
    let familyDeleteDialog: FamilyDeleteDialog;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Families', async () => {
        await navBarPage.goToEntity('family');
        familyComponentsPage = new FamilyComponentsPage();
        expect(await familyComponentsPage.getTitle()).toMatch(/fglgatewayApp.family.home.title/);
    });

    it('should load create Family page', async () => {
        await familyComponentsPage.clickOnCreateButton();
        familyUpdatePage = new FamilyUpdatePage();
        expect(await familyUpdatePage.getPageTitle()).toMatch(/fglgatewayApp.family.home.createOrEditLabel/);
        await familyUpdatePage.cancel();
    });

    it('should create and save Families', async () => {
        await familyComponentsPage.clickOnCreateButton();
        await familyUpdatePage.setNameInput('name');
        expect(await familyUpdatePage.getNameInput()).toMatch('name');
        // familyUpdatePage.membersSelectLastOption();
        await familyUpdatePage.save();
        expect(await familyUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    it('should delete last Family', async () => {
        const nbButtonsBeforeDelete = await familyComponentsPage.countDeleteButtons();
        await familyComponentsPage.clickOnLastDeleteButton();

        familyDeleteDialog = new FamilyDeleteDialog();
        expect(await familyDeleteDialog.getDialogTitle()).toMatch(/fglgatewayApp.family.delete.question/);
        await familyDeleteDialog.clickOnConfirmButton();

        expect(await familyComponentsPage.countDeleteButtons()).toBe(nbButtonsBeforeDelete - 1);
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});

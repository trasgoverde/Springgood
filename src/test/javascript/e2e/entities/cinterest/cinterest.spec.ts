import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CinterestComponentsPage, CinterestDeleteDialog, CinterestUpdatePage } from './cinterest.page-object';

const expect = chai.expect;

describe('Cinterest e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cinterestComponentsPage: CinterestComponentsPage;
  let cinterestUpdatePage: CinterestUpdatePage;
  let cinterestDeleteDialog: CinterestDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Cinterests', async () => {
    await navBarPage.goToEntity('cinterest');
    cinterestComponentsPage = new CinterestComponentsPage();
    await browser.wait(ec.visibilityOf(cinterestComponentsPage.title), 5000);
    expect(await cinterestComponentsPage.getTitle()).to.eq('springgoodApp.cinterest.home.title');
    await browser.wait(ec.or(ec.visibilityOf(cinterestComponentsPage.entities), ec.visibilityOf(cinterestComponentsPage.noResult)), 1000);
  });

  it('should load create Cinterest page', async () => {
    await cinterestComponentsPage.clickOnCreateButton();
    cinterestUpdatePage = new CinterestUpdatePage();
    expect(await cinterestUpdatePage.getPageTitle()).to.eq('springgoodApp.cinterest.home.createOrEditLabel');
    await cinterestUpdatePage.cancel();
  });

  it('should create and save Cinterests', async () => {
    const nbButtonsBeforeCreate = await cinterestComponentsPage.countDeleteButtons();

    await cinterestComponentsPage.clickOnCreateButton();

    await promise.all([
      cinterestUpdatePage.setInterestNameInput('interestName'),
      // cinterestUpdatePage.communitySelectLastOption(),
    ]);

    expect(await cinterestUpdatePage.getInterestNameInput()).to.eq(
      'interestName',
      'Expected InterestName value to be equals to interestName'
    );

    await cinterestUpdatePage.save();
    expect(await cinterestUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cinterestComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Cinterest', async () => {
    const nbButtonsBeforeDelete = await cinterestComponentsPage.countDeleteButtons();
    await cinterestComponentsPage.clickOnLastDeleteButton();

    cinterestDeleteDialog = new CinterestDeleteDialog();
    expect(await cinterestDeleteDialog.getDialogTitle()).to.eq('springgoodApp.cinterest.delete.question');
    await cinterestDeleteDialog.clickOnConfirmButton();

    expect(await cinterestComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CmessageComponentsPage, CmessageDeleteDialog, CmessageUpdatePage } from './cmessage.page-object';

const expect = chai.expect;

describe('Cmessage e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cmessageComponentsPage: CmessageComponentsPage;
  let cmessageUpdatePage: CmessageUpdatePage;
  let cmessageDeleteDialog: CmessageDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Cmessages', async () => {
    await navBarPage.goToEntity('cmessage');
    cmessageComponentsPage = new CmessageComponentsPage();
    await browser.wait(ec.visibilityOf(cmessageComponentsPage.title), 5000);
    expect(await cmessageComponentsPage.getTitle()).to.eq('springgoodApp.cmessage.home.title');
    await browser.wait(ec.or(ec.visibilityOf(cmessageComponentsPage.entities), ec.visibilityOf(cmessageComponentsPage.noResult)), 1000);
  });

  it('should load create Cmessage page', async () => {
    await cmessageComponentsPage.clickOnCreateButton();
    cmessageUpdatePage = new CmessageUpdatePage();
    expect(await cmessageUpdatePage.getPageTitle()).to.eq('springgoodApp.cmessage.home.createOrEditLabel');
    await cmessageUpdatePage.cancel();
  });

  it('should create and save Cmessages', async () => {
    const nbButtonsBeforeCreate = await cmessageComponentsPage.countDeleteButtons();

    await cmessageComponentsPage.clickOnCreateButton();

    await promise.all([
      cmessageUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      cmessageUpdatePage.setCmessageTextInput('cmessageText'),
      cmessageUpdatePage.csenderSelectLastOption(),
      cmessageUpdatePage.creceiverSelectLastOption(),
    ]);

    expect(await cmessageUpdatePage.getCreationDateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected creationDate value to be equals to 2000-12-31'
    );
    expect(await cmessageUpdatePage.getCmessageTextInput()).to.eq(
      'cmessageText',
      'Expected CmessageText value to be equals to cmessageText'
    );
    const selectedIsDelivered = cmessageUpdatePage.getIsDeliveredInput();
    if (await selectedIsDelivered.isSelected()) {
      await cmessageUpdatePage.getIsDeliveredInput().click();
      expect(await cmessageUpdatePage.getIsDeliveredInput().isSelected(), 'Expected isDelivered not to be selected').to.be.false;
    } else {
      await cmessageUpdatePage.getIsDeliveredInput().click();
      expect(await cmessageUpdatePage.getIsDeliveredInput().isSelected(), 'Expected isDelivered to be selected').to.be.true;
    }

    await cmessageUpdatePage.save();
    expect(await cmessageUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cmessageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Cmessage', async () => {
    const nbButtonsBeforeDelete = await cmessageComponentsPage.countDeleteButtons();
    await cmessageComponentsPage.clickOnLastDeleteButton();

    cmessageDeleteDialog = new CmessageDeleteDialog();
    expect(await cmessageDeleteDialog.getDialogTitle()).to.eq('springgoodApp.cmessage.delete.question');
    await cmessageDeleteDialog.clickOnConfirmButton();

    expect(await cmessageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

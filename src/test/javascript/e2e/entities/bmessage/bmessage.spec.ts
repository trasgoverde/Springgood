import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { BmessageComponentsPage, BmessageDeleteDialog, BmessageUpdatePage } from './bmessage.page-object';

const expect = chai.expect;

describe('Bmessage e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let bmessageComponentsPage: BmessageComponentsPage;
  let bmessageUpdatePage: BmessageUpdatePage;
  let bmessageDeleteDialog: BmessageDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Bmessages', async () => {
    await navBarPage.goToEntity('bmessage');
    bmessageComponentsPage = new BmessageComponentsPage();
    await browser.wait(ec.visibilityOf(bmessageComponentsPage.title), 5000);
    expect(await bmessageComponentsPage.getTitle()).to.eq('springgoodApp.bmessage.home.title');
    await browser.wait(ec.or(ec.visibilityOf(bmessageComponentsPage.entities), ec.visibilityOf(bmessageComponentsPage.noResult)), 1000);
  });

  it('should load create Bmessage page', async () => {
    await bmessageComponentsPage.clickOnCreateButton();
    bmessageUpdatePage = new BmessageUpdatePage();
    expect(await bmessageUpdatePage.getPageTitle()).to.eq('springgoodApp.bmessage.home.createOrEditLabel');
    await bmessageUpdatePage.cancel();
  });

  it('should create and save Bmessages', async () => {
    const nbButtonsBeforeCreate = await bmessageComponentsPage.countDeleteButtons();

    await bmessageComponentsPage.clickOnCreateButton();

    await promise.all([
      bmessageUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      bmessageUpdatePage.setBmessageTextInput('bmessageText'),
    ]);

    expect(await bmessageUpdatePage.getCreationDateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected creationDate value to be equals to 2000-12-31'
    );
    expect(await bmessageUpdatePage.getBmessageTextInput()).to.eq(
      'bmessageText',
      'Expected BmessageText value to be equals to bmessageText'
    );
    const selectedIsDelivered = bmessageUpdatePage.getIsDeliveredInput();
    if (await selectedIsDelivered.isSelected()) {
      await bmessageUpdatePage.getIsDeliveredInput().click();
      expect(await bmessageUpdatePage.getIsDeliveredInput().isSelected(), 'Expected isDelivered not to be selected').to.be.false;
    } else {
      await bmessageUpdatePage.getIsDeliveredInput().click();
      expect(await bmessageUpdatePage.getIsDeliveredInput().isSelected(), 'Expected isDelivered to be selected').to.be.true;
    }

    await bmessageUpdatePage.save();
    expect(await bmessageUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await bmessageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Bmessage', async () => {
    const nbButtonsBeforeDelete = await bmessageComponentsPage.countDeleteButtons();
    await bmessageComponentsPage.clickOnLastDeleteButton();

    bmessageDeleteDialog = new BmessageDeleteDialog();
    expect(await bmessageDeleteDialog.getDialogTitle()).to.eq('springgoodApp.bmessage.delete.question');
    await bmessageDeleteDialog.clickOnConfirmButton();

    expect(await bmessageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

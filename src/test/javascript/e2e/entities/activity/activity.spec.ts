import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ActivityComponentsPage, ActivityDeleteDialog, ActivityUpdatePage } from './activity.page-object';

const expect = chai.expect;

describe('Activity e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let activityComponentsPage: ActivityComponentsPage;
  let activityUpdatePage: ActivityUpdatePage;
  let activityDeleteDialog: ActivityDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Activities', async () => {
    await navBarPage.goToEntity('activity');
    activityComponentsPage = new ActivityComponentsPage();
    await browser.wait(ec.visibilityOf(activityComponentsPage.title), 5000);
    expect(await activityComponentsPage.getTitle()).to.eq('springgoodApp.activity.home.title');
    await browser.wait(ec.or(ec.visibilityOf(activityComponentsPage.entities), ec.visibilityOf(activityComponentsPage.noResult)), 1000);
  });

  it('should load create Activity page', async () => {
    await activityComponentsPage.clickOnCreateButton();
    activityUpdatePage = new ActivityUpdatePage();
    expect(await activityUpdatePage.getPageTitle()).to.eq('springgoodApp.activity.home.createOrEditLabel');
    await activityUpdatePage.cancel();
  });

  it('should create and save Activities', async () => {
    const nbButtonsBeforeCreate = await activityComponentsPage.countDeleteButtons();

    await activityComponentsPage.clickOnCreateButton();

    await promise.all([
      activityUpdatePage.setActivityNameInput('activityName'),
      // activityUpdatePage.appuserSelectLastOption(),
    ]);

    expect(await activityUpdatePage.getActivityNameInput()).to.eq(
      'activityName',
      'Expected ActivityName value to be equals to activityName'
    );

    await activityUpdatePage.save();
    expect(await activityUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await activityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Activity', async () => {
    const nbButtonsBeforeDelete = await activityComponentsPage.countDeleteButtons();
    await activityComponentsPage.clickOnLastDeleteButton();

    activityDeleteDialog = new ActivityDeleteDialog();
    expect(await activityDeleteDialog.getDialogTitle()).to.eq('springgoodApp.activity.delete.question');
    await activityDeleteDialog.clickOnConfirmButton();

    expect(await activityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

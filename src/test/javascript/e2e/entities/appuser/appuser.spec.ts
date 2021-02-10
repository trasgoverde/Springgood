import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { AppuserComponentsPage, AppuserDeleteDialog, AppuserUpdatePage } from './appuser.page-object';

const expect = chai.expect;

describe('Appuser e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let appuserComponentsPage: AppuserComponentsPage;
  let appuserUpdatePage: AppuserUpdatePage;
  let appuserDeleteDialog: AppuserDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Appusers', async () => {
    await navBarPage.goToEntity('appuser');
    appuserComponentsPage = new AppuserComponentsPage();
    await browser.wait(ec.visibilityOf(appuserComponentsPage.title), 5000);
    expect(await appuserComponentsPage.getTitle()).to.eq('springgoodApp.appuser.home.title');
    await browser.wait(ec.or(ec.visibilityOf(appuserComponentsPage.entities), ec.visibilityOf(appuserComponentsPage.noResult)), 1000);
  });

  it('should load create Appuser page', async () => {
    await appuserComponentsPage.clickOnCreateButton();
    appuserUpdatePage = new AppuserUpdatePage();
    expect(await appuserUpdatePage.getPageTitle()).to.eq('springgoodApp.appuser.home.createOrEditLabel');
    await appuserUpdatePage.cancel();
  });

  it('should create and save Appusers', async () => {
    const nbButtonsBeforeCreate = await appuserComponentsPage.countDeleteButtons();

    await appuserComponentsPage.clickOnCreateButton();

    await promise.all([
      appuserUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      appuserUpdatePage.setAssignedVotesPointsInput('5'),
      appuserUpdatePage.communitySelectLastOption(),
      appuserUpdatePage.blogSelectLastOption(),
      appuserUpdatePage.notificationSelectLastOption(),
      appuserUpdatePage.albumSelectLastOption(),
      appuserUpdatePage.commentSelectLastOption(),
      appuserUpdatePage.postSelectLastOption(),
      appuserUpdatePage.senderSelectLastOption(),
      appuserUpdatePage.receiverSelectLastOption(),
      appuserUpdatePage.followedSelectLastOption(),
      appuserUpdatePage.followingSelectLastOption(),
      appuserUpdatePage.blockeduserSelectLastOption(),
      appuserUpdatePage.blockinguserSelectLastOption(),
      appuserUpdatePage.vtopicSelectLastOption(),
      appuserUpdatePage.vquestionSelectLastOption(),
      appuserUpdatePage.vanswerSelectLastOption(),
      appuserUpdatePage.vthumbSelectLastOption(),
      appuserUpdatePage.proposalSelectLastOption(),
      appuserUpdatePage.proposalVoteSelectLastOption(),
    ]);

    expect(await appuserUpdatePage.getCreationDateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected creationDate value to be equals to 2000-12-31'
    );
    expect(await appuserUpdatePage.getAssignedVotesPointsInput()).to.eq('5', 'Expected assignedVotesPoints value to be equals to 5');

    await appuserUpdatePage.save();
    expect(await appuserUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await appuserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Appuser', async () => {
    const nbButtonsBeforeDelete = await appuserComponentsPage.countDeleteButtons();
    await appuserComponentsPage.clickOnLastDeleteButton();

    appuserDeleteDialog = new AppuserDeleteDialog();
    expect(await appuserDeleteDialog.getDialogTitle()).to.eq('springgoodApp.appuser.delete.question');
    await appuserDeleteDialog.clickOnConfirmButton();

    expect(await appuserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

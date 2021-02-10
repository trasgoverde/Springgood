import { element, by, ElementFinder } from 'protractor';

export class AppuserComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-appuser div table .btn-danger'));
  title = element.all(by.css('jhi-appuser div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class AppuserUpdatePage {
  pageTitle = element(by.id('jhi-appuser-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  creationDateInput = element(by.id('field_creationDate'));
  assignedVotesPointsInput = element(by.id('field_assignedVotesPoints'));

  communitySelect = element(by.id('field_community'));
  blogSelect = element(by.id('field_blog'));
  notificationSelect = element(by.id('field_notification'));
  albumSelect = element(by.id('field_album'));
  commentSelect = element(by.id('field_comment'));
  postSelect = element(by.id('field_post'));
  senderSelect = element(by.id('field_sender'));
  receiverSelect = element(by.id('field_receiver'));
  followedSelect = element(by.id('field_followed'));
  followingSelect = element(by.id('field_following'));
  blockeduserSelect = element(by.id('field_blockeduser'));
  blockinguserSelect = element(by.id('field_blockinguser'));
  vtopicSelect = element(by.id('field_vtopic'));
  vquestionSelect = element(by.id('field_vquestion'));
  vanswerSelect = element(by.id('field_vanswer'));
  vthumbSelect = element(by.id('field_vthumb'));
  proposalSelect = element(by.id('field_proposal'));
  proposalVoteSelect = element(by.id('field_proposalVote'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCreationDateInput(creationDate: string): Promise<void> {
    await this.creationDateInput.sendKeys(creationDate);
  }

  async getCreationDateInput(): Promise<string> {
    return await this.creationDateInput.getAttribute('value');
  }

  async setAssignedVotesPointsInput(assignedVotesPoints: string): Promise<void> {
    await this.assignedVotesPointsInput.sendKeys(assignedVotesPoints);
  }

  async getAssignedVotesPointsInput(): Promise<string> {
    return await this.assignedVotesPointsInput.getAttribute('value');
  }

  async communitySelectLastOption(): Promise<void> {
    await this.communitySelect.all(by.tagName('option')).last().click();
  }

  async communitySelectOption(option: string): Promise<void> {
    await this.communitySelect.sendKeys(option);
  }

  getCommunitySelect(): ElementFinder {
    return this.communitySelect;
  }

  async getCommunitySelectedOption(): Promise<string> {
    return await this.communitySelect.element(by.css('option:checked')).getText();
  }

  async blogSelectLastOption(): Promise<void> {
    await this.blogSelect.all(by.tagName('option')).last().click();
  }

  async blogSelectOption(option: string): Promise<void> {
    await this.blogSelect.sendKeys(option);
  }

  getBlogSelect(): ElementFinder {
    return this.blogSelect;
  }

  async getBlogSelectedOption(): Promise<string> {
    return await this.blogSelect.element(by.css('option:checked')).getText();
  }

  async notificationSelectLastOption(): Promise<void> {
    await this.notificationSelect.all(by.tagName('option')).last().click();
  }

  async notificationSelectOption(option: string): Promise<void> {
    await this.notificationSelect.sendKeys(option);
  }

  getNotificationSelect(): ElementFinder {
    return this.notificationSelect;
  }

  async getNotificationSelectedOption(): Promise<string> {
    return await this.notificationSelect.element(by.css('option:checked')).getText();
  }

  async albumSelectLastOption(): Promise<void> {
    await this.albumSelect.all(by.tagName('option')).last().click();
  }

  async albumSelectOption(option: string): Promise<void> {
    await this.albumSelect.sendKeys(option);
  }

  getAlbumSelect(): ElementFinder {
    return this.albumSelect;
  }

  async getAlbumSelectedOption(): Promise<string> {
    return await this.albumSelect.element(by.css('option:checked')).getText();
  }

  async commentSelectLastOption(): Promise<void> {
    await this.commentSelect.all(by.tagName('option')).last().click();
  }

  async commentSelectOption(option: string): Promise<void> {
    await this.commentSelect.sendKeys(option);
  }

  getCommentSelect(): ElementFinder {
    return this.commentSelect;
  }

  async getCommentSelectedOption(): Promise<string> {
    return await this.commentSelect.element(by.css('option:checked')).getText();
  }

  async postSelectLastOption(): Promise<void> {
    await this.postSelect.all(by.tagName('option')).last().click();
  }

  async postSelectOption(option: string): Promise<void> {
    await this.postSelect.sendKeys(option);
  }

  getPostSelect(): ElementFinder {
    return this.postSelect;
  }

  async getPostSelectedOption(): Promise<string> {
    return await this.postSelect.element(by.css('option:checked')).getText();
  }

  async senderSelectLastOption(): Promise<void> {
    await this.senderSelect.all(by.tagName('option')).last().click();
  }

  async senderSelectOption(option: string): Promise<void> {
    await this.senderSelect.sendKeys(option);
  }

  getSenderSelect(): ElementFinder {
    return this.senderSelect;
  }

  async getSenderSelectedOption(): Promise<string> {
    return await this.senderSelect.element(by.css('option:checked')).getText();
  }

  async receiverSelectLastOption(): Promise<void> {
    await this.receiverSelect.all(by.tagName('option')).last().click();
  }

  async receiverSelectOption(option: string): Promise<void> {
    await this.receiverSelect.sendKeys(option);
  }

  getReceiverSelect(): ElementFinder {
    return this.receiverSelect;
  }

  async getReceiverSelectedOption(): Promise<string> {
    return await this.receiverSelect.element(by.css('option:checked')).getText();
  }

  async followedSelectLastOption(): Promise<void> {
    await this.followedSelect.all(by.tagName('option')).last().click();
  }

  async followedSelectOption(option: string): Promise<void> {
    await this.followedSelect.sendKeys(option);
  }

  getFollowedSelect(): ElementFinder {
    return this.followedSelect;
  }

  async getFollowedSelectedOption(): Promise<string> {
    return await this.followedSelect.element(by.css('option:checked')).getText();
  }

  async followingSelectLastOption(): Promise<void> {
    await this.followingSelect.all(by.tagName('option')).last().click();
  }

  async followingSelectOption(option: string): Promise<void> {
    await this.followingSelect.sendKeys(option);
  }

  getFollowingSelect(): ElementFinder {
    return this.followingSelect;
  }

  async getFollowingSelectedOption(): Promise<string> {
    return await this.followingSelect.element(by.css('option:checked')).getText();
  }

  async blockeduserSelectLastOption(): Promise<void> {
    await this.blockeduserSelect.all(by.tagName('option')).last().click();
  }

  async blockeduserSelectOption(option: string): Promise<void> {
    await this.blockeduserSelect.sendKeys(option);
  }

  getBlockeduserSelect(): ElementFinder {
    return this.blockeduserSelect;
  }

  async getBlockeduserSelectedOption(): Promise<string> {
    return await this.blockeduserSelect.element(by.css('option:checked')).getText();
  }

  async blockinguserSelectLastOption(): Promise<void> {
    await this.blockinguserSelect.all(by.tagName('option')).last().click();
  }

  async blockinguserSelectOption(option: string): Promise<void> {
    await this.blockinguserSelect.sendKeys(option);
  }

  getBlockinguserSelect(): ElementFinder {
    return this.blockinguserSelect;
  }

  async getBlockinguserSelectedOption(): Promise<string> {
    return await this.blockinguserSelect.element(by.css('option:checked')).getText();
  }

  async vtopicSelectLastOption(): Promise<void> {
    await this.vtopicSelect.all(by.tagName('option')).last().click();
  }

  async vtopicSelectOption(option: string): Promise<void> {
    await this.vtopicSelect.sendKeys(option);
  }

  getVtopicSelect(): ElementFinder {
    return this.vtopicSelect;
  }

  async getVtopicSelectedOption(): Promise<string> {
    return await this.vtopicSelect.element(by.css('option:checked')).getText();
  }

  async vquestionSelectLastOption(): Promise<void> {
    await this.vquestionSelect.all(by.tagName('option')).last().click();
  }

  async vquestionSelectOption(option: string): Promise<void> {
    await this.vquestionSelect.sendKeys(option);
  }

  getVquestionSelect(): ElementFinder {
    return this.vquestionSelect;
  }

  async getVquestionSelectedOption(): Promise<string> {
    return await this.vquestionSelect.element(by.css('option:checked')).getText();
  }

  async vanswerSelectLastOption(): Promise<void> {
    await this.vanswerSelect.all(by.tagName('option')).last().click();
  }

  async vanswerSelectOption(option: string): Promise<void> {
    await this.vanswerSelect.sendKeys(option);
  }

  getVanswerSelect(): ElementFinder {
    return this.vanswerSelect;
  }

  async getVanswerSelectedOption(): Promise<string> {
    return await this.vanswerSelect.element(by.css('option:checked')).getText();
  }

  async vthumbSelectLastOption(): Promise<void> {
    await this.vthumbSelect.all(by.tagName('option')).last().click();
  }

  async vthumbSelectOption(option: string): Promise<void> {
    await this.vthumbSelect.sendKeys(option);
  }

  getVthumbSelect(): ElementFinder {
    return this.vthumbSelect;
  }

  async getVthumbSelectedOption(): Promise<string> {
    return await this.vthumbSelect.element(by.css('option:checked')).getText();
  }

  async proposalSelectLastOption(): Promise<void> {
    await this.proposalSelect.all(by.tagName('option')).last().click();
  }

  async proposalSelectOption(option: string): Promise<void> {
    await this.proposalSelect.sendKeys(option);
  }

  getProposalSelect(): ElementFinder {
    return this.proposalSelect;
  }

  async getProposalSelectedOption(): Promise<string> {
    return await this.proposalSelect.element(by.css('option:checked')).getText();
  }

  async proposalVoteSelectLastOption(): Promise<void> {
    await this.proposalVoteSelect.all(by.tagName('option')).last().click();
  }

  async proposalVoteSelectOption(option: string): Promise<void> {
    await this.proposalVoteSelect.sendKeys(option);
  }

  getProposalVoteSelect(): ElementFinder {
    return this.proposalVoteSelect;
  }

  async getProposalVoteSelectedOption(): Promise<string> {
    return await this.proposalVoteSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class AppuserDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-appuser-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-appuser'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}

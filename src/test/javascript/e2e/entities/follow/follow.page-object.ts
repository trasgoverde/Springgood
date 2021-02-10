import { element, by, ElementFinder } from 'protractor';

export class FollowComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-follow div table .btn-danger'));
  title = element.all(by.css('jhi-follow div h2#page-heading span')).first();
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

export class FollowUpdatePage {
  pageTitle = element(by.id('jhi-follow-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  creationDateInput = element(by.id('field_creationDate'));

  cfollowedSelect = element(by.id('field_cfollowed'));
  cfollowingSelect = element(by.id('field_cfollowing'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCreationDateInput(creationDate: string): Promise<void> {
    await this.creationDateInput.sendKeys(creationDate);
  }

  async getCreationDateInput(): Promise<string> {
    return await this.creationDateInput.getAttribute('value');
  }

  async cfollowedSelectLastOption(): Promise<void> {
    await this.cfollowedSelect.all(by.tagName('option')).last().click();
  }

  async cfollowedSelectOption(option: string): Promise<void> {
    await this.cfollowedSelect.sendKeys(option);
  }

  getCfollowedSelect(): ElementFinder {
    return this.cfollowedSelect;
  }

  async getCfollowedSelectedOption(): Promise<string> {
    return await this.cfollowedSelect.element(by.css('option:checked')).getText();
  }

  async cfollowingSelectLastOption(): Promise<void> {
    await this.cfollowingSelect.all(by.tagName('option')).last().click();
  }

  async cfollowingSelectOption(option: string): Promise<void> {
    await this.cfollowingSelect.sendKeys(option);
  }

  getCfollowingSelect(): ElementFinder {
    return this.cfollowingSelect;
  }

  async getCfollowingSelectedOption(): Promise<string> {
    return await this.cfollowingSelect.element(by.css('option:checked')).getText();
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

export class FollowDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-follow-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-follow'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}

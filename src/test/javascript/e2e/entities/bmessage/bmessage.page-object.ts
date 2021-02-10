import { element, by, ElementFinder } from 'protractor';

export class BmessageComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-bmessage div table .btn-danger'));
  title = element.all(by.css('jhi-bmessage div h2#page-heading span')).first();
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

export class BmessageUpdatePage {
  pageTitle = element(by.id('jhi-bmessage-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  creationDateInput = element(by.id('field_creationDate'));
  bmessageTextInput = element(by.id('field_bmessageText'));
  isDeliveredInput = element(by.id('field_isDelivered'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCreationDateInput(creationDate: string): Promise<void> {
    await this.creationDateInput.sendKeys(creationDate);
  }

  async getCreationDateInput(): Promise<string> {
    return await this.creationDateInput.getAttribute('value');
  }

  async setBmessageTextInput(bmessageText: string): Promise<void> {
    await this.bmessageTextInput.sendKeys(bmessageText);
  }

  async getBmessageTextInput(): Promise<string> {
    return await this.bmessageTextInput.getAttribute('value');
  }

  getIsDeliveredInput(): ElementFinder {
    return this.isDeliveredInput;
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

export class BmessageDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-bmessage-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-bmessage'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}

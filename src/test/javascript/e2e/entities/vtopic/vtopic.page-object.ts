import { element, by, ElementFinder } from 'protractor';

export class VtopicComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-vtopic div table .btn-danger'));
  title = element.all(by.css('jhi-vtopic div h2#page-heading span')).first();
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

export class VtopicUpdatePage {
  pageTitle = element(by.id('jhi-vtopic-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  creationDateInput = element(by.id('field_creationDate'));
  vtopicTitleInput = element(by.id('field_vtopicTitle'));
  vtopicDescriptionInput = element(by.id('field_vtopicDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCreationDateInput(creationDate: string): Promise<void> {
    await this.creationDateInput.sendKeys(creationDate);
  }

  async getCreationDateInput(): Promise<string> {
    return await this.creationDateInput.getAttribute('value');
  }

  async setVtopicTitleInput(vtopicTitle: string): Promise<void> {
    await this.vtopicTitleInput.sendKeys(vtopicTitle);
  }

  async getVtopicTitleInput(): Promise<string> {
    return await this.vtopicTitleInput.getAttribute('value');
  }

  async setVtopicDescriptionInput(vtopicDescription: string): Promise<void> {
    await this.vtopicDescriptionInput.sendKeys(vtopicDescription);
  }

  async getVtopicDescriptionInput(): Promise<string> {
    return await this.vtopicDescriptionInput.getAttribute('value');
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

export class VtopicDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-vtopic-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-vtopic'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}

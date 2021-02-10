import { element, by, ElementFinder } from 'protractor';

export class VquestionComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-vquestion div table .btn-danger'));
  title = element.all(by.css('jhi-vquestion div h2#page-heading span')).first();
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

export class VquestionUpdatePage {
  pageTitle = element(by.id('jhi-vquestion-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  creationDateInput = element(by.id('field_creationDate'));
  vquestionInput = element(by.id('field_vquestion'));
  vquestionDescriptionInput = element(by.id('field_vquestionDescription'));

  vtopicSelect = element(by.id('field_vtopic'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCreationDateInput(creationDate: string): Promise<void> {
    await this.creationDateInput.sendKeys(creationDate);
  }

  async getCreationDateInput(): Promise<string> {
    return await this.creationDateInput.getAttribute('value');
  }

  async setVquestionInput(vquestion: string): Promise<void> {
    await this.vquestionInput.sendKeys(vquestion);
  }

  async getVquestionInput(): Promise<string> {
    return await this.vquestionInput.getAttribute('value');
  }

  async setVquestionDescriptionInput(vquestionDescription: string): Promise<void> {
    await this.vquestionDescriptionInput.sendKeys(vquestionDescription);
  }

  async getVquestionDescriptionInput(): Promise<string> {
    return await this.vquestionDescriptionInput.getAttribute('value');
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

export class VquestionDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-vquestion-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-vquestion'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}

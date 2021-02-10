import { element, by, ElementFinder } from 'protractor';

export class VanswerComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-vanswer div table .btn-danger'));
  title = element.all(by.css('jhi-vanswer div h2#page-heading span')).first();
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

export class VanswerUpdatePage {
  pageTitle = element(by.id('jhi-vanswer-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  creationDateInput = element(by.id('field_creationDate'));
  urlVanswerInput = element(by.id('field_urlVanswer'));
  acceptedInput = element(by.id('field_accepted'));

  vquestionSelect = element(by.id('field_vquestion'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCreationDateInput(creationDate: string): Promise<void> {
    await this.creationDateInput.sendKeys(creationDate);
  }

  async getCreationDateInput(): Promise<string> {
    return await this.creationDateInput.getAttribute('value');
  }

  async setUrlVanswerInput(urlVanswer: string): Promise<void> {
    await this.urlVanswerInput.sendKeys(urlVanswer);
  }

  async getUrlVanswerInput(): Promise<string> {
    return await this.urlVanswerInput.getAttribute('value');
  }

  getAcceptedInput(): ElementFinder {
    return this.acceptedInput;
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

export class VanswerDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-vanswer-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-vanswer'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}

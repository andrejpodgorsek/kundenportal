import { element, by, ElementFinder } from 'protractor';

export class AgenturComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-agentur div table .btn-danger'));
  title = element.all(by.css('jhi-agentur div h2#page-heading span')).first();
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

export class AgenturUpdatePage {
  pageTitle = element(by.id('jhi-agentur-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  agenturnummerInput = element(by.id('field_agenturnummer'));
  nameInput = element(by.id('field_name'));
  adresseInput = element(by.id('field_adresse'));
  emailInput = element(by.id('field_email'));
  telefonnrInput = element(by.id('field_telefonnr'));
  createdInput = element(by.id('field_created'));

  userSelect = element(by.id('field_user'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setAgenturnummerInput(agenturnummer: string): Promise<void> {
    await this.agenturnummerInput.sendKeys(agenturnummer);
  }

  async getAgenturnummerInput(): Promise<string> {
    return await this.agenturnummerInput.getAttribute('value');
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setAdresseInput(adresse: string): Promise<void> {
    await this.adresseInput.sendKeys(adresse);
  }

  async getAdresseInput(): Promise<string> {
    return await this.adresseInput.getAttribute('value');
  }

  async setEmailInput(email: string): Promise<void> {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput(): Promise<string> {
    return await this.emailInput.getAttribute('value');
  }

  async setTelefonnrInput(telefonnr: string): Promise<void> {
    await this.telefonnrInput.sendKeys(telefonnr);
  }

  async getTelefonnrInput(): Promise<string> {
    return await this.telefonnrInput.getAttribute('value');
  }

  async setCreatedInput(created: string): Promise<void> {
    await this.createdInput.sendKeys(created);
  }

  async getCreatedInput(): Promise<string> {
    return await this.createdInput.getAttribute('value');
  }

  async userSelectLastOption(): Promise<void> {
    await this.userSelect.all(by.tagName('option')).last().click();
  }

  async userSelectOption(option: string): Promise<void> {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect(): ElementFinder {
    return this.userSelect;
  }

  async getUserSelectedOption(): Promise<string> {
    return await this.userSelect.element(by.css('option:checked')).getText();
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

export class AgenturDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-agentur-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-agentur'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}

<script setup>
import {
  Button,
  IftaLabel,
  InputText,
  InputNumber,
  Select,
  SelectButton,
  Textarea,
  Stepper,
  StepList,
  StepPanels,
  StepPanel,
  Step,
  DatePicker,
  Message,
} from 'primevue'
import { ref } from 'vue'
import { useToastNotifier } from '@/composables/useToast'

defineProps(['coordinates', 'disciplines', 'people', 'locations'])

const emit = defineEmits(['closeForm'])

const emitCloseForm = () => {
  emit('closeForm')
}

const labworkName = ref()
const labworkDescription = ref()
const labworkDifficulty = ref()
const labworkMinimalPoint = ref()
const labworkAveragePoint = ref()

const disciplineName = ref()
const disciplinePracticeHours = ref()

const coordinatesX = ref()
const coordinatesY = ref()

const authorName = ref()
const authorEyeColor = ref()
const authorHairColor = ref()
const authorBirthday = ref()
const authorNationality = ref()

const locationName = ref()
const locationX = ref()
const locationY = ref()
const locationZ = ref()

const difficulties = ['VERY_EASY', 'NORMAL', 'INSANE', 'IMPOSSIBLE']
const colors = ['BLACK', 'BLUE', 'YELLOW', 'BROWN']
const countries = ['UNITED_KINGDOM', 'USA', 'FRANCE', 'SOUTH_KOREA', 'NORTH_KOREA']

const modeOptions = ref([
  { label: 'Select existing', value: 'existing' },
  { label: 'Create new', value: 'new' },
])

const coordinatesMode = ref('existing')
const disciplineMode = ref('existing')
const authorMode = ref('existing')
const locationMode = ref('existing')

const selectedCoordinates = ref()
const selectedDiscipline = ref()
const selectedAuthor = ref()
const selectedLocation = ref()

const isLabworkNameValid = ref(true)
const isLabworkMinimalPointValid = ref(true)
const isLabworkAveragePointValid = ref(true)
const isDisciplineNameValid = ref(true)
const isDisciplinePracticeHoursValid = ref(true)
const isCoordinatesXValid = ref(true)
const isCoordinatesYValid = ref(true)
const isAuthorNameValid = ref(true)
const isAuthorHairColorValid = ref(true)
const isAuthorBirthdayValid = ref(true)
const isAuthorNationalityValid = ref(true)
const isLocationNameValid = ref(true)
const isLocationXValid = ref(true)
const isLocationYValid = ref(true)
const isLocationZValid = ref(true)

const { bakeToast } = useToastNotifier()

const validateStage1 = (activateCallback) => {
  isLabworkNameValid.value = true
  isLabworkMinimalPointValid.value = true
  isLabworkAveragePointValid.value = true

  if (labworkName.value === undefined || labworkName.value === '') {
    isLabworkNameValid.value = false
  }

  if (
    labworkMinimalPoint.value !== undefined &&
    labworkMinimalPoint.value !== null &&
    labworkMinimalPoint.value <= 0
  ) {
    isLabworkMinimalPointValid.value = false
  }

  if (
    labworkAveragePoint.value === undefined ||
    labworkAveragePoint.value === null ||
    labworkAveragePoint.value <= 0
  ) {
    isLabworkAveragePointValid.value = false
  }

  if (
    isLabworkNameValid.value &&
    isLabworkMinimalPointValid.value &&
    isLabworkAveragePointValid.value
  ) {
    activateCallback('2')
  }
}

const validateStage2 = (activateCallback) => {
  if (selectedDiscipline.value && disciplineMode.value === 'existing') {
    activateCallback('3')
  }

  isDisciplineNameValid.value = true
  isDisciplinePracticeHoursValid.value = true

  if (disciplineName.value === undefined || disciplineName.value === '') {
    isDisciplineNameValid.value = false
  }

  if (
    disciplinePracticeHours.value === undefined ||
    disciplinePracticeHours.value === null ||
    disciplinePracticeHours.value < 1
  ) {
    isDisciplinePracticeHoursValid.value = false
  }

  if (isDisciplineNameValid.value && isDisciplinePracticeHoursValid.value) {
    activateCallback('3')
  }
}

const validateStage3 = (activateCallback) => {
  if (selectedCoordinates.value && coordinatesMode.value === 'existing') {
    activateCallback('4')
  }

  isCoordinatesXValid.value = true
  isCoordinatesYValid.value = true

  if (coordinatesX.value === undefined || coordinatesX.value === null) {
    isCoordinatesXValid.value = false
  }

  if (
    coordinatesY.value === undefined ||
    coordinatesY.value === null ||
    coordinatesY.value < -566
  ) {
    isCoordinatesYValid.value = false
  }

  if (isCoordinatesXValid.value && isCoordinatesYValid.value) {
    activateCallback('4')
  }
}

const validateStage4 = (activateCallback) => {
  if (selectedAuthor.value && authorMode.value === 'existing') {
    activateCallback('5')
  }

  isAuthorNameValid.value = true
  isAuthorHairColorValid.value = true
  isAuthorBirthdayValid.value = true
  isAuthorNationalityValid.value = true

  if (authorName.value === undefined || authorName.value === '') {
    isAuthorNameValid.value = false
  }

  if (authorHairColor.value === undefined) {
    isAuthorHairColorValid.value = false
  }

  if (authorBirthday.value === undefined) {
    isAuthorBirthdayValid.value = false
  }

  if (authorNationality.value === undefined) {
    isAuthorNationalityValid.value = false
  }

  if (
    isAuthorNameValid.value &&
    isAuthorHairColorValid.value &&
    isAuthorBirthdayValid.value &&
    isAuthorNationalityValid.value
  ) {
    activateCallback('5')
  }
}

const validateStage5 = () => {
  if (selectedLocation.value && locationMode.value === 'existing') {
    createEntry()
  }

  isLocationNameValid.value = true
  isLocationXValid.value = true
  isLocationYValid.value = true
  isLocationZValid.value = true

  if (
    locationName.value === undefined ||
    locationName.value === '' ||
    locationName.value.length > 246
  ) {
    isLocationNameValid.value = false
  }

  if (locationX.value === undefined || locationX.value === null) {
    isLocationXValid.value = false
  }

  if (locationY.value === undefined || locationY.value === null) {
    isLocationYValid.value = false
  }

  if (locationZ.value === undefined || locationZ.value === null) {
    isLocationZValid.value = false
  }

  if (
    isLocationNameValid.value &&
    isLocationXValid.value &&
    isLocationYValid.value &&
    isLocationZValid.value
  ) {
    createEntry()
  }
}

async function createEntry() {
  const body = {
    name: labworkName.value,
    description: labworkDescription.value,
    difficulty: labworkDifficulty.value,
    minimalPoint: labworkMinimalPoint.value,
    averagePoint: labworkAveragePoint.value,
    discipline: selectedDiscipline.value
      ? selectedDiscipline.value
      : {
          name: disciplineName.value,
          practiceHours: disciplinePracticeHours.value,
        },
    coordinates: selectedCoordinates.value
      ? selectedCoordinates.value
      : {
          x: coordinatesX.value,
          y: coordinatesY.value,
        },
    author: selectedAuthor.value
      ? selectedAuthor.value
      : {
          name: authorName.value,
          eyeColor: authorEyeColor.value,
          hairColor: authorHairColor.value,
          birthday: new Date(authorBirthday.value).toISOString().slice(0, -1),
          nationality: authorNationality.value,
          location: selectedLocation.value
            ? selectedLocation.value
            : {
                name: locationName.value,
                x: locationX.value,
                y: locationY.value,
                z: locationZ.value,
              },
        },
  }

  const response = await fetch('http://localhost:8080/lab1/api/labwork', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(body),
  })
  const data = await response.json()

  bakeToast(data.string, response.ok)

  if (response.ok) {
    emitCloseForm()
  }
}
</script>

<template>
  <Stepper value="1" linear>
    <StepList>
      <Step value="1">Lab work parameters</Step>
      <Step value="2">Discipline</Step>
      <Step value="3">Coordinates</Step>
      <Step value="4">Author</Step>
      <Step value="5">Location</Step>
    </StepList>
    <StepPanels>
      <StepPanel v-slot="{ activateCallback }" value="1">
        <IftaLabel>
          <InputText
            id="formNameInput"
            v-model="labworkName"
            variant="filled"
            placeholder="Required"
          ></InputText>
          <Message v-if="!isLabworkNameValid" severity="error"
            >Field is required to proceed</Message
          >
          <label for="formNameInput">Name</label>
        </IftaLabel>

        <IftaLabel>
          <Textarea
            id="formDescriptionInput"
            v-model="labworkDescription"
            variant="filled"
          ></Textarea>
          <label for="formDescriptionInput">Description</label>
        </IftaLabel>

        <IftaLabel>
          <Select
            id="formDifficultyInput"
            v-model="labworkDifficulty"
            :options="difficulties"
            variant="filled"
          ></Select>
          <label for="formDifficultyInput">Difficulty</label>
        </IftaLabel>

        <IftaLabel>
          <InputNumber
            id="formMinimalPointInput"
            v-model="labworkMinimalPoint"
            variant="filled"
            :use-grouping="false"
            :min-fraction-digits="0"
            :max-fraction-digits="5"
            placeholder="Greater than 0"
          ></InputNumber>
          <Message v-if="!isLabworkMinimalPointValid" severity="error"
            >Field needs to be greater than 0</Message
          >
          <label for="formMinimalPointInput">Minimal point</label>
        </IftaLabel>

        <IftaLabel>
          <InputNumber
            id="formAveragePointInput"
            v-model="labworkAveragePoint"
            variant="filled"
            :use-grouping="false"
            :min-fraction-digits="0"
            :max-fraction-digits="5"
            placeholder="Required; value > 0"
          ></InputNumber>
          <Message v-if="!isLabworkAveragePointValid" severity="error"
            >Field is required to proceed and needs to be greater than 0</Message
          >
          <label for="formAveragePointInput">Average point</label>
        </IftaLabel>

        <div style="display: flex; justify-content: flex-end">
          <Button label="Next" @click="validateStage1(activateCallback)"></Button>
        </div>
      </StepPanel>

      <StepPanel v-slot="{ activateCallback }" value="2">
        <SelectButton
          v-model="disciplineMode"
          :options="modeOptions"
          option-label="label"
          option-value="value"
        ></SelectButton>

        <div v-if="disciplineMode === 'existing'">
          <Select
            v-model="selectedDiscipline"
            :options="disciplines"
            placeholder="Select existing discipline"
            variant="filled"
            option-label="name"
          ></Select>
        </div>

        <div v-else>
          <IftaLabel>
            <InputText
              id="formDisciplineNameInput"
              v-model="disciplineName"
              variant="filled"
              placeholder="Required"
            ></InputText>
            <Message v-if="!isDisciplineNameValid" severity="error"
              >Field is required to proceed</Message
            >
            <label for="formDisciplineNameInput">Discipline name</label>
          </IftaLabel>

          <IftaLabel>
            <InputNumber
              id="formDisciplinePracticeHoursInput"
              v-model="disciplinePracticeHours"
              variant="filled"
              :use-grouping="false"
              placeholder="Required; value >= 1"
            ></InputNumber>
            <Message v-if="!isDisciplinePracticeHoursValid" severity="error"
              >Field is required to proceed and needs to be greater than or equal to 1</Message
            >
            <label for="formDisciplinePracticeHoursInput">Practice hours</label>
          </IftaLabel>
        </div>

        <div style="display: flex; justify-content: space-between">
          <Button label="Back" @click="activateCallback('1')"></Button>
          <Button label="Next" @click="validateStage2(activateCallback)"></Button>
        </div>
      </StepPanel>

      <StepPanel v-slot="{ activateCallback }" value="3">
        <SelectButton
          v-model="coordinatesMode"
          :options="modeOptions"
          option-label="label"
          option-value="value"
        ></SelectButton>

        <div v-if="coordinatesMode === 'existing'">
          <Select
            v-model="selectedCoordinates"
            :options="coordinates"
            placeholder="Select existing coordinates"
            variant="filled"
          >
            <template #value="slotProps">
              <div v-if="slotProps.value">
                X: {{ slotProps.value.x }}, Y: {{ slotProps.value.y }}
              </div>
              <div v-else>
                {{ slotProps.placeholder }}
              </div>
            </template>
            <template #option="slotProps">
              <div>X: {{ slotProps.option.x }}, Y: {{ slotProps.option.y }}</div>
            </template>
          </Select>
        </div>

        <div v-else>
          <IftaLabel>
            <InputNumber
              id="formCoordinatesXInput"
              v-model="coordinatesX"
              variant="filled"
              :use-grouping="false"
              :min-fraction-digits="0"
              :max-fraction-digits="5"
              placeholder="Required"
            ></InputNumber>
            <Message v-if="!isCoordinatesXValid" severity="error"
              >Field is required to proceed</Message
            >
            <label for="formCoordinatesXInput">X coordinate</label>
          </IftaLabel>

          <IftaLabel>
            <InputNumber
              id="formCoordinatesYInput"
              v-model="coordinatesY"
              variant="filled"
              :use-grouping="false"
              :min-fraction-digits="0"
              :max-fraction-digits="5"
              placeholder="Required; value >= -566"
            ></InputNumber>
            <Message v-if="!isCoordinatesYValid" severity="error"
              >Field is required to proceed and needs to be greater than or equal to -566</Message
            >
            <label for="formCoordinatesYInput">Y coordinate</label>
          </IftaLabel>
        </div>

        <div style="display: flex; justify-content: space-between">
          <Button label="Back" @click="activateCallback('2')"></Button>
          <Button label="Next" @click="validateStage3(activateCallback)"></Button>
        </div>
      </StepPanel>

      <StepPanel v-slot="{ activateCallback }" value="4">
        <SelectButton
          v-model="authorMode"
          :options="modeOptions"
          option-label="label"
          option-value="value"
        ></SelectButton>

        <div v-if="authorMode === 'existing'">
          <Select
            v-model="selectedAuthor"
            :options="people"
            placeholder="Select existing author"
            variant="filled"
            option-label="name"
          >
          </Select>
        </div>

        <div v-else>
          <IftaLabel>
            <InputText
              id="formAuthorNameInput"
              v-model="authorName"
              variant="filled"
              placeholder="Required"
            ></InputText>
            <Message v-if="!isAuthorNameValid" severity="error"
              >Field is required to proceed</Message
            >
            <label for="formAuthorNameInput">Author name</label>
          </IftaLabel>

          <IftaLabel>
            <Select
              id="formAuthorEyeColorInput"
              v-model="authorEyeColor"
              :options="colors"
              variant="filled"
            ></Select>
            <label for="formAuthorEyeColorInput">Eye color</label>
          </IftaLabel>

          <IftaLabel>
            <Select
              id="formAuthorHairColorInput"
              v-model="authorHairColor"
              :options="colors"
              variant="filled"
              placeholder="Required"
            ></Select>
            <Message v-if="!isAuthorHairColorValid" severity="error"
              >Field is required to proceed</Message
            >
            <label for="formAuthorHairColorInput">Hair color</label>
          </IftaLabel>

          <IftaLabel>
            <DatePicker
              id="formAuthorBirthdayInput"
              v-model="authorBirthday"
              variant="filled"
              placeholder="Required"
            ></DatePicker>
            <Message v-if="!isAuthorBirthdayValid" severity="error"
              >Field is required to proceed</Message
            >
            <label for="formAuthorBirthdayInput">Birthday</label>
          </IftaLabel>

          <IftaLabel>
            <Select
              id="formAuthorNationalityInput"
              v-model="authorNationality"
              :options="countries"
              variant="filled"
              placeholder="Required"
            ></Select>
            <Message v-if="!isAuthorNationalityValid" severity="error"
              >Field is required to proceed</Message
            >
            <label for="formAuthorNationalityInput">Nationality</label>
          </IftaLabel>
        </div>

        <div style="display: flex; justify-content: space-between">
          <Button label="Back" @click="activateCallback('3')"></Button>
          <Button
            :label="selectedAuthor ? 'Create' : 'Next'"
            @click="selectedAuthor ? createEntry() : validateStage4(activateCallback)"
          ></Button>
        </div>
      </StepPanel>

      <StepPanel v-slot="{ activateCallback }" value="5">
        <SelectButton
          v-model="locationMode"
          :options="modeOptions"
          option-label="label"
          option-value="value"
        ></SelectButton>

        <div v-if="locationMode === 'existing'">
          <Select
            v-model="selectedLocation"
            :options="locations"
            placeholder="Select existing location"
            variant="filled"
            option-label="name"
          >
          </Select>
        </div>

        <div v-else>
          <IftaLabel>
            <InputText
              id="formLocationNameInput"
              v-model="locationName"
              variant="filled"
              placeholder="Required; 246 characters at most"
            ></InputText>
            <Message v-if="!isLocationNameValid" severity="error"
              >Field is required to proceed and can be 246 characters long at most</Message
            >
            <label for="formLocationNameInput">Location name</label>
          </IftaLabel>

          <IftaLabel>
            <InputNumber
              id="formLocationXInput"
              v-model="locationX"
              variant="filled"
              :use-grouping="false"
              placeholder="Required"
            ></InputNumber>
            <Message v-if="!isLocationXValid" severity="error"
              >Field is required to proceed</Message
            >
            <label for="formLocationXInput">X coordinate</label>
          </IftaLabel>

          <IftaLabel>
            <InputNumber
              id="formLocationYInput"
              v-model="locationY"
              variant="filled"
              :use-grouping="false"
              :min-fraction-digits="0"
              :max-fraction-digits="5"
              placeholder="Required"
            ></InputNumber>
            <Message v-if="!isLocationYValid" severity="error"
              >Field is required to proceed</Message
            >
            <label for="formLocationYInput">Y coordinate</label>
          </IftaLabel>

          <IftaLabel>
            <InputNumber
              id="formLocationZInput"
              v-model="locationZ"
              variant="filled"
              :use-grouping="false"
              :min-fraction-digits="0"
              :max-fraction-digits="5"
              placeholder="Required"
            ></InputNumber>
            <Message v-if="!isLocationZValid" severity="error"
              >Field is required to proceed</Message
            >
            <label for="formLocationZInput">Z coordinate</label>
          </IftaLabel>
        </div>

        <div style="display: flex; justify-content: space-between">
          <Button label="Back" @click="activateCallback('4')"></Button>
          <Button label="Create" severity="success" @click="validateStage5"></Button>
        </div>
      </StepPanel>
    </StepPanels>
  </Stepper>
</template>

<style scoped>
.p-textarea,
.p-select,
.p-selectbutton,
.p-inputtext,
.p-inputnumber,
.p-datepicker,
.p-message {
  margin-bottom: 1.5rem;
}

:deep(.p-step-title) {
  font-family: 'Tektur', sans-serif !important;
}
</style>

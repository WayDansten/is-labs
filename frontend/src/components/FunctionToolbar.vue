<script setup>
import { Button, InputGroup, InputText, InputNumber, Toolbar, Popover } from 'primevue'
import { ref } from 'vue'
import { useToastNotifier } from '@/composables/useToast'

const emit = defineEmits(['createEntry'])

const emitCreateEntry = () => {
  emit('createEntry')
}

const { bakeToast } = useToastNotifier()

const deleteByIdValue = ref()
const deleteByAuthorValue = ref()
const countByAveragePointValue = ref()
const getByDescriptionValue = ref()
const lowerDifficultyIdValue = ref()
const lowerDifficultyStepsValue = ref()

const countByAveragePointPop = ref()
const getByDescriptionPop = ref()
const lowerDifficultyPop = ref()
const deleteByIdPop = ref()
const deleteByAuthorPop = ref()

// Popover toggles

const toggleCountByAveragePoint = (event) => {
  countByAveragePointPop.value.toggle(event)
}

const toggleGetByDescription = (event) => {
  getByDescriptionPop.value.toggle(event)
}

const toggleLowerDifficulty = (event) => {
  lowerDifficultyPop.value.toggle(event)
}

const toggleDeleteById = (event) => {
  deleteByIdPop.value.toggle(event)
}

const toggleDeleteByAuthor = (event) => {
  deleteByAuthorPop.value.toggle(event)
}

// Request fetching functions

const deleteById = async (id) => {
  if (id === undefined || id === null) {
    bakeToast('"LabWork ID" field is empty', false)
  } else {
    const response = await fetch(`http://localhost:8080/lab1/api/labwork/${id}`, {
      method: 'DELETE',
    })
    const data = await response.json()

    bakeToast(data.string, response.ok)
  }
}

defineExpose({ deleteById })

async function deleteByAuthor() {
  if (
    deleteByAuthorValue.value === undefined ||
    deleteByAuthorValue.value === null ||
    deleteByAuthorValue.value === ''
  ) {
    bakeToast('"Author name" field is empty', false)
  } else {
    const response = await fetch('http://localhost:8080/lab1/api/labwork/author', {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ string: deleteByAuthorValue.value }),
    })
    const data = await response.json()

    bakeToast(data.string, response.ok)
  }
}

async function countByAveragePoint() {
  if (countByAveragePointValue.value === undefined || countByAveragePointValue.value === null) {
    bakeToast('"Average point value" field is empty', false)
  } else {
    const params = new URLSearchParams({ averagePoint: countByAveragePointValue.value })
    const response = await fetch(`http://localhost:8080/lab1/api/labwork/average_point?${params}`)
    const data = await response.json()

    bakeToast('Lab works found: ' + data.value, response.ok)
  }
}

async function getByDescription() {
  if (getByDescriptionValue.value === undefined || getByDescriptionValue.value === '') {
    bakeToast('"Description prefix" field is empty', false)
  } else {
    const params = new URLSearchParams({ prefix: getByDescriptionValue.value })
    const response = await fetch(`http://localhost:8080/lab1/api/labwork/description?${params}`)
    const data = await response.json()

    bakeToast(data.string, response.ok)
  }
}

async function lowerDifficulty() {
  if (lowerDifficultyIdValue.value === undefined || lowerDifficultyIdValue.value === null) {
    bakeToast('"LabWork ID" field is empty', false)
  } else if (
    lowerDifficultyStepsValue.value === undefined ||
    lowerDifficultyStepsValue.value === null
  ) {
    bakeToast('"Difficulty" field is empty', false)
  } else {
    const response = await fetch(`http://localhost:8080/lab1/api/labwork/difficulty`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        id: lowerDifficultyIdValue.value,
        steps: lowerDifficultyStepsValue.value,
      }),
    })
    const data = await response.json()

    bakeToast(data.string, response.ok)
  }
}
</script>

<template>
  <Toolbar>
    <template #center>
      <Button
        label="Create new entry"
        size="small"
        severity="info"
        @click="emitCreateEntry"
      ></Button>

      <Button
        label="Count by greater Average Point"
        size="small"
        severity="warn"
        @click="toggleCountByAveragePoint"
      ></Button>
      <Popover ref="countByAveragePointPop">
        <InputGroup>
          <InputNumber
            v-model="countByAveragePointValue"
            variant="filled"
            :use-grouping="false"
            :min-fraction-digits="0"
            :max-fraction-digits="5"
            placeholder="Average point value"
          ></InputNumber>
          <Button label="Submit" @click="countByAveragePoint"></Button>
        </InputGroup>
      </Popover>

      <Button
        label="Get by description"
        size="small"
        severity="warn"
        @click="toggleGetByDescription"
      ></Button>
      <Popover ref="getByDescriptionPop">
        <InputGroup>
          <InputText
            v-model="getByDescriptionValue"
            variant="filled"
            placeholder="Description prefix"
          ></InputText>
          <Button label="Submit" @click="getByDescription"></Button>
        </InputGroup>
      </Popover>

      <Button
        label="Lower the Difficulty"
        size="small"
        severity="warn"
        @click="toggleLowerDifficulty"
      ></Button>
      <Popover ref="lowerDifficultyPop">
        <InputGroup>
          <InputNumber
            v-model="lowerDifficultyIdValue"
            variant="filled"
            placeholder="Lab work ID"
            :use-grouping="false"
          ></InputNumber>
          <InputNumber
            v-model="lowerDifficultyStepsValue"
            placeholder="Steps"
            :use-grouping="false"
            :min="1"
            :max="3"
          ></InputNumber>
          <Button label="Submit" @click="lowerDifficulty"></Button> </InputGroup
      ></Popover>

      <Button label="Delete by ID" size="small" severity="warn" @click="toggleDeleteById"></Button>
      <Popover ref="deleteByIdPop">
        <InputGroup>
          <InputNumber
            v-model="deleteByIdValue"
            variant="filled"
            placeholder="Lab work ID"
            :use-grouping="false"
          ></InputNumber>
          <Button label="Submit" @click="deleteById(deleteByIdValue)"></Button> </InputGroup
      ></Popover>

      <Button
        label="Delete by Author"
        size="small"
        severity="warn"
        @click="toggleDeleteByAuthor"
      ></Button>
      <Popover ref="deleteByAuthorPop">
        <InputGroup>
          <InputText
            v-model="deleteByAuthorValue"
            variant="filled"
            placeholder="Author name"
          ></InputText>
          <Button label="Submit" @click="deleteByAuthor"></Button> </InputGroup
      ></Popover>
    </template>
  </Toolbar>
</template>

<style scoped>
.p-toolbar {
  background-color: rgba(0, 0, 0, 0.35);
  border: none;
  border-radius: 0;
}

.p-toolbar .p-button {
  margin: 0.5rem;
}
</style>
